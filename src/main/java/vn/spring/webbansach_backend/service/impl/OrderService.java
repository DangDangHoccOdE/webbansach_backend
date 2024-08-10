package vn.spring.webbansach_backend.service.impl;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import vn.spring.webbansach_backend.dao.OrderRepository;
import vn.spring.webbansach_backend.dto.BookDtoOfOrder;
import vn.spring.webbansach_backend.dto.OrderDto;
import vn.spring.webbansach_backend.entity.*;
import vn.spring.webbansach_backend.service.inter.*;
import vn.spring.webbansach_backend.utils.ConvertStringToDate;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderService implements IOrderService {
    private final OrderRepository orderRepository;
    private final ICartItemService iCartItemService;
    private final IDeliveryService iDeliveryService;
    private final IPaymentService iPaymentService;
    private final UserService userService;
    private final IBookService iBookService;
    @Autowired
    public OrderService(OrderRepository orderRepository, ICartItemService iCartItemService, IDeliveryService iDeliveryService, IPaymentService iPaymentService, UserService userService, IBookService iBookService) {
        this.orderRepository = orderRepository;
        this.iCartItemService = iCartItemService;
        this.iDeliveryService = iDeliveryService;
        this.iPaymentService = iPaymentService;
        this.userService = userService;
        this.iBookService = iBookService;
    }

    @Override
    @Transactional
    public Order save(Order order) {
        return orderRepository.save(order);
    }

    @Override
    public Order findOrderById(Long orderId) {
        return orderRepository.findByOrderId(orderId);
    }

    @Override
    @Transactional
    public ResponseEntity<?> confirmReceivedOrder(Long orderId) {
        Order order = findOrderById(orderId);
        if(order == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Notice("Không tìm thấy đơn hàng!"));
        }

        order.setOrderStatus("Hoàn thành");
        orderRepository.save(order);

        return ResponseEntity.ok(new Notice("Xác nhận đã nhận đơn hàng thành công"));
    }

    @Override
    public ResponseEntity<?> cancelOder(Long orderId) {
        Order order = findOrderById(orderId);
        if(order == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Notice("Không tìm thấy đơn hàng!"));
        }

        if(order.getOrderStatus().equals("Đã hủy")){
            return ResponseEntity.badRequest().body(new Notice("Hủy đơn hàng thất bại"));
        }
        order.setOrderStatus("Đã hủy");
        order.setDeliveryStatus("");
        orderRepository.save(order);
        return ResponseEntity.ok(new Notice("Đã hủy đơn hàng thành công"));
    }

    @Override
    @Transactional
    public ResponseEntity<?> repurchase(Long orderId) {
        Order order = findOrderById(orderId);
        if(order == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Notice("Không tìm thấy đơn hàng!"));
        }

        User user = order.getUser();
        List<OrderDetail> orderDetailList = order.getOrderDetailList();
        List<Long> cartItemIds = new ArrayList<>();

        for(OrderDetail orderDetail : orderDetailList){
            Book book = orderDetail.getBook();
            CartItem cartItem = new CartItem();
            cartItem.setBooks(book);
            cartItem.setQuantity(orderDetail.getQuantity());
            cartItem.setUser(user);
            cartItem.setCreatedAt(LocalDateTime.now());
            iCartItemService.saveCartItem(cartItem);
            cartItemIds.add(cartItem.getCartItemId());
        }

        return ResponseEntity.ok(cartItemIds);
    }

    @Override
    public ResponseEntity<?> getBooksOfOrder(Long orderId) {
        Order order = findOrderById(orderId);
        if(order == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Notice("Không tìm thấy đơn hàng!"));
        }

        List<OrderDetail> orderDetailList = order.getOrderDetailList();
        List<BookDtoOfOrder> books = orderDetailList.stream()
                .map(orderDetail->{
                    Book book = orderDetail.getBook();
                    return new BookDtoOfOrder(
                            book.getBookId(),
                            book.getBookName(),
                            book.getPrice(),
                            book.getListedPrice()
                    );
                }).collect(Collectors.toList());

        return ResponseEntity.ok(books);
    }

    @Override
    @Transactional
    public ResponseEntity<?> addOrder(OrderDto orderDto,boolean isBuyNow) {
        if(orderRepository.findByOrderCode(orderDto.getOrderCode())!=null){
            return ResponseEntity.badRequest().body(new Notice("Tạo đơn hàng không thành công, lỗi mã đơn hàng tồn tại"));
        }
        // Tạo order
        Order order = new Order();
        order.setOrderCode(orderDto.getOrderCode());
        order.setDate(ConvertStringToDate.convertToLocalDateTime(orderDto.getDate()));
        order.setTotalProduct(orderDto.getTotalProduct());
        order.setDeliveryAddress(orderDto.getDeliveryAddress());
        order.setPaymentCost(orderDto.getPaymentCost());
        order.setOrderStatus(orderDto.getOrderStatus());
        order.setNoteFromUser(orderDto.getNoteFromUser());
        order.setDeliveryStatus(orderDto.getDeliveryStatus());
        order.setPurchaseAddress(orderDto.getPurchaseAddress());
        order.setShippingFee(orderDto.getShippingFeeVoucher());
        order.setTotalPrice(orderDto.getTotalPrice());

        List<OrderDetail> oderDetails = new ArrayList<>();
        // Tạo OrderDetail
            List<Integer> cartItemIdList = orderDto.getCartItems();
            for(Integer cartItemId:cartItemIdList) {
                if(!isBuyNow) { // Nếu không phải người dùng ấn nút MUA NGAY
                    CartItem cartItem = iCartItemService.findCartItemById(cartItemId);
                    if (cartItem == null) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Notice("Không tìm thấy sản phẩm trong giỏ hàng!"));
                    }
                    Integer bookId = cartItem.getBooks().getBookId();
                    addOrderAndUpdateBook(oderDetails,bookId,cartItem.getQuantity(),order);
                    iCartItemService.deleteCartItem(cartItem.getCartItemId()); // Xóa các sản phẩm trong giỏ hàng sau khi xác nhận mua hàng

                }else{
                    // Vì khi người dùng ấn vào mua ngay thì không tạo sản phẩm trong cartItem nên id cartItem sẽ là idBook
                    Integer bookId = orderDto.getCartItems().get(0);
                    int totalBook = orderDto.getTotalProduct();
                    addOrderAndUpdateBook(oderDetails,bookId,totalBook,order);
            }
        }

        // Tạo đối tượng payment
        Payment payment = addAndUpdatePayment(orderDto.getPaymentMethod());

        // Tạo đối tượng delivery
        Delivery delivery = addAndUpdateDelivery(orderDto.getDeliveryMethod(),orderDto.getShippingFee());

        // Lấy user
        User user = userService.findUserByUserId(orderDto.getUserId());
        if(user==null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Notice("Không tìm thấy người sử dụng"));
        }
        order.setUser(user);
        order.setOrderDetailList(oderDetails);
        order.setPayment(payment);
        order.setDelivery(delivery);

        orderRepository.save(order);
        return ResponseEntity.ok(new Notice("Đã tạo đơn hàng thành công, cảm ơn bạn đã tin tưởng dùng sản phẩm"));
    }

    private Payment addAndUpdatePayment(String paymentMethod){
        Payment payment = iPaymentService.findByPaymentName(paymentMethod);
        if(payment == null){ // Nếu phương thức thanh toán chưa tồn tại
            payment = new Payment();
            payment.setPaymentName(paymentMethod);
            iPaymentService.save(payment);
        }
        return payment;
    }

    private Delivery addAndUpdateDelivery(String deliveryMethod,double shippingFee){
        Delivery delivery = iDeliveryService.findByDeliveryName(deliveryMethod);
        if(delivery == null){ // Nếu phương thức thanh toán chưa tồn tại
            delivery = new Delivery();
            delivery.setDeliveryName(deliveryMethod);
            delivery.setShippingFee(shippingFee);
            iDeliveryService.save(delivery);
        }

        return delivery;
    }

    private void addOrderAndUpdateBook(List<OrderDetail> orderDetails,Integer bookId, int quantity, Order order){
        Book book = iBookService.findBookById(bookId);
        if(book == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy sách!");
        }

        book.setSoldQuantity(book.getSoldQuantity()+quantity);
        iBookService.save(book);

        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setQuantity(quantity);
        orderDetail.setPrice(quantity * book.getPrice());
        orderDetail.setBook(book);
        orderDetail.setOrder(order);

        orderDetails.add(orderDetail);
    }
}
