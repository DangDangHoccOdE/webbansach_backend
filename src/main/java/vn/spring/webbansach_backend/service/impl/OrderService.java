package vn.spring.webbansach_backend.service.impl;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import vn.spring.webbansach_backend.dao.DeliveryRepository;
import vn.spring.webbansach_backend.dao.OrderRepository;
import vn.spring.webbansach_backend.dao.PaymentRepository;
import vn.spring.webbansach_backend.dto.OrderDto;
import vn.spring.webbansach_backend.entity.*;
import vn.spring.webbansach_backend.service.inter.IOrderService;
import vn.spring.webbansach_backend.utils.ConvertStringToDate;
import java.util.*;

@Service
public class OrderService implements IOrderService {
    private final OrderRepository orderRepository;
    private final CartItemService cartItemService;
    private final DeliveryService deliveryService;
    private final PaymentService paymentService;
    private final PaymentRepository paymentRepository;
    private final DeliveryRepository deliveryRepository;
    private final UserService userService;

    @Autowired
    OrderService(OrderRepository orderRepository, CartItemService cartItemService, DeliveryService deliveryService, PaymentService paymentService,
                 PaymentRepository paymentRepository,
                 DeliveryRepository deliveryRepository, UserService userService) {
        this.orderRepository = orderRepository;
        this.cartItemService = cartItemService;
        this.deliveryService = deliveryService;
        this.paymentService = paymentService;
        this.paymentRepository = paymentRepository;
        this.deliveryRepository = deliveryRepository;
        this.userService = userService;
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
            CartItem cartItem = cartItemService.findCartItemById(i);
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
        Payment payment = paymentService.findByPaymentName(orderDto.getPaymentMethod());
        if(payment == null){ // Nếu phương thức thanh toán chưa tồn tại
            payment = new Payment();
            payment.setPaymentName(orderDto.getPaymentMethod());
            paymentRepository.save(payment);
        }

        // Tạo đối tượng delivery
        Delivery delivery = deliveryService.findByDeliveryName(orderDto.getDeliveryMethod());
        if(delivery == null){ // Nếu phương thức thanh toán chưa tồn tại
            delivery = new Delivery();
            delivery.setDeliveryName(orderDto.getDeliveryMethod());
            delivery.setShippingFee(orderDto.getShippingFee());
            deliveryRepository.save(delivery);
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
