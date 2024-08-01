package vn.spring.webbansach_backend.service.inter;
import org.springframework.http.ResponseEntity;
import vn.spring.webbansach_backend.entity.Order;
import vn.spring.webbansach_backend.entity.OrderDetail;

import java.util.*;
public interface IOrderDetailService {
    ResponseEntity<?> getOrderDetailsFromOrder(Long orderId);
}
