package vn.spring.webbansach_backend.service.impl;

import jakarta.transaction.Transactional;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import vn.spring.webbansach_backend.dao.DeliveryRepository;
import vn.spring.webbansach_backend.dao.OrderRepository;
import vn.spring.webbansach_backend.dao.PaymentRepository;
import vn.spring.webbansach_backend.dto.BookDtoOfOrder;
import vn.spring.webbansach_backend.dto.OrderDto;
import vn.spring.webbansach_backend.entity.*;
import vn.spring.webbansach_backend.service.inter.*;
import vn.spring.webbansach_backend.utils.ConvertStringToDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderService implements IOrderService {
    private final OrderRepository orderRepository;
    private final ICartItemService iCartItemService;
    private final IDeliveryService iDeliveryService;
    private final IPaymentService iPaymentService;
    private final UserService userService;
    private final IOrderDetailService iOrderDetailService;
    @Autowired
    public OrderService(OrderRepository orderRepository, ICartItemService iCartItemService, IDeliveryService iDeliveryService, IPaymentService iPaymentService, UserService userService,@Lazy IOrderDetailService iOrderDetailService) {
        this.orderRepository = orderRepository;
        this.iCartItemService = iCartItemService;
        this.iDeliveryService = iDeliveryService;
        this.iPaymentService = iPaymentService;
        this.userService = userService;
        this.iOrderDetailService = iOrderDetailService;
    }

    @Override
    public Order findOrderById(Long orderId) {
        return orderRepository.findByOrderId(orderId);
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
    public ResponseEntity<?> addOrder(OrderDto orderDto) {
        // Tạo order
        Order order = new Order();
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

        // Tạo OrderDetail
        List<Integer> cartItemIdList = orderDto.getCartItems();
        List<OrderDetail> oderDetails = new ArrayList<>();
        for(Integer i:cartItemIdList){
            CartItem cartItem = iCartItemService.findCartItemById(i);
            if(cartItem==null){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Notice("Không tìm thấy sản phẩm trong giỏ hàng!"));
            }
            Book book = cartItem.getBooks();

            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setQuantity(cartItem.getQuantity());
            orderDetail.setPrice(cartItem.getQuantity() * book.getPrice());
            orderDetail.setBook(book);
            orderDetail.setOrder(order);

            oderDetails.add(orderDetail);
        }

        // Tạo đối tượng payment
        Payment payment = iPaymentService.findByPaymentName(orderDto.getPaymentMethod());
        if(payment == null){ // Nếu phương thức thanh toán chưa tồn tại
            payment = new Payment();
            payment.setPaymentName(orderDto.getPaymentMethod());
            iPaymentService.save(payment);
        }

        // Tạo đối tượng delivery
        Delivery delivery = iDeliveryService.findByDeliveryName(orderDto.getDeliveryMethod());
        if(delivery == null){ // Nếu phương thức thanh toán chưa tồn tại
            delivery = new Delivery();
            delivery.setDeliveryName(orderDto.getDeliveryMethod());
            delivery.setShippingFee(orderDto.getShippingFee());
            iDeliveryService.save(delivery);
        }

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
}
