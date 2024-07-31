package vn.spring.webbansach_backend.service.inter;

import org.springframework.http.ResponseEntity;
import vn.spring.webbansach_backend.dto.OrderDto;

public interface IOrderService {
    ResponseEntity<?> addOrder(OrderDto orderDto);

}
