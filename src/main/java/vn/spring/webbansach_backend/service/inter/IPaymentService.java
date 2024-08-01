package vn.spring.webbansach_backend.service.inter;

import vn.spring.webbansach_backend.entity.Payment;

public interface IPaymentService {
    Payment findByPaymentName(String paymentName);
    void save(Payment payment);
}
