package vn.spring.webbansach_backend.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.spring.webbansach_backend.dao.PaymentRepository;
import vn.spring.webbansach_backend.entity.Payment;
import vn.spring.webbansach_backend.service.inter.IPaymentService;
@Service
public class PaymentService implements IPaymentService {
    private final PaymentRepository paymentRepository;
    @Autowired
    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Override
    public Payment findByPaymentName(String paymentName) {
        return paymentRepository.findByPaymentName(paymentName);
    }

    @Override
    public void save(Payment payment) {
        paymentRepository.save(payment);
    }
}
