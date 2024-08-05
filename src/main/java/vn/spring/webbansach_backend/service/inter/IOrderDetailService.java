package vn.spring.webbansach_backend.service.inter;
import org.springframework.http.ResponseEntity;

public interface IOrderDetailService {
    ResponseEntity<?> getOrderDetailsFromOrder(Long orderId);
}
