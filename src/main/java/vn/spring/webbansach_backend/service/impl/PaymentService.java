package vn.spring.webbansach_backend.service.impl;

import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import vn.spring.webbansach_backend.dao.PaymentRepository;
import vn.spring.webbansach_backend.dto.PaymentDto;
import vn.spring.webbansach_backend.entity.Notice;
import vn.spring.webbansach_backend.entity.Order;
import vn.spring.webbansach_backend.entity.Payment;
import vn.spring.webbansach_backend.service.inter.IOrderService;
import vn.spring.webbansach_backend.service.inter.IPaymentService;
@Service
public class PaymentService implements IPaymentService {
    private final PaymentRepository paymentRepository;
    private final IOrderService iOrderService;
    @Autowired
    public PaymentService(PaymentRepository paymentRepository, IOrderService iOrderService) {
        this.paymentRepository = paymentRepository;
        this.iOrderService = iOrderService;
    }

    @Override
    public Payment findByPaymentName(String paymentName) {
        return paymentRepository.findByPaymentName(paymentName);
    }

    @Override
    public void save(Payment payment) {
        paymentRepository.save(payment);
    }

    @Override
    public ResponseEntity<?> findPaymentByOrderId(Long orderId) {
        Order order = iOrderService.findOrderById(orderId);

        if(order == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Notice("Không tìm thấy đơn hàng!"));
        }

        PaymentDto paymentDto = new PaymentDto();
        paymentDto.setPaymentName(order.getPayment().getPaymentName());
        paymentDto.setPaymentId(order.getPayment().getPaymentId());

        return ResponseEntity.ok().body(paymentDto);
    }
}
