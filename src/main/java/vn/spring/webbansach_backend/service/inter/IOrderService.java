package vn.spring.webbansach_backend.service.inter;

import org.springframework.http.ResponseEntity;
import vn.spring.webbansach_backend.dto.OrderDto;
import vn.spring.webbansach_backend.entity.Order;

public interface IOrderService {
    Order findOrderById(Long orderId);
    ResponseEntity<?> addOrder(OrderDto orderDto);
    ResponseEntity<?> cancelOder(Long orderId);
    ResponseEntity<?> repurchase(Long orderId);
    ResponseEntity<?> getBooksOfOrder(Long orderId);

}
