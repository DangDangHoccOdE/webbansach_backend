package vn.spring.webbansach_backend.service.inter;

import org.springframework.http.ResponseEntity;
import vn.spring.webbansach_backend.dto.OrderDto;
import vn.spring.webbansach_backend.entity.Orders;

public interface IOrderService {
    Orders findOrderById(Long orderId);
    ResponseEntity<?> addOrder(OrderDto orderDto);
    ResponseEntity<?> cancelOder(Long orderId);
    ResponseEntity<?> repurchase(Long orderId);
    ResponseEntity<?> confirmReceivedOrder(Long orderId);
    ResponseEntity<?> getBooksOfOrder(Long orderId);

}
