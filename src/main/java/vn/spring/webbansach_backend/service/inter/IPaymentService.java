package vn.spring.webbansach_backend.service.inter;

import org.springframework.http.ResponseEntity;
import vn.spring.webbansach_backend.entity.Payment;

public interface IPaymentService {
    Payment findByPaymentName(String paymentName);
    void save(Payment payment);

    ResponseEntity<?> findPaymentByOrderId(Long orderId);
}
