package vn.spring.webbansach_backend.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import vn.spring.webbansach_backend.entity.Payment;

@RepositoryRestResource(path = "payment")
public interface PaymentRepository extends JpaRepository<Payment,Integer> {
    Payment findByPaymentName(String paymentName);
}

