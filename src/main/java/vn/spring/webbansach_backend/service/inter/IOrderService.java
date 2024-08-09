package vn.spring.webbansach_backend.service.inter;

import org.springframework.http.ResponseEntity;
import vn.spring.webbansach_backend.dto.OrderDto;
import vn.spring.webbansach_backend.entity.Order;

public interface IOrderService {
    Order findOrderById(Long orderId);
    Order save(Order order);
    ResponseEntity<?> addOrder(OrderDto orderDto,boolean isBuyNow);
    ResponseEntity<?> cancelOder(Long orderId);
    ResponseEntity<?> repurchase(Long orderId);
    ResponseEntity<?> confirmReceivedOrder(Long orderId);
    ResponseEntity<?> getBooksOfOrder(Long orderId);

}
