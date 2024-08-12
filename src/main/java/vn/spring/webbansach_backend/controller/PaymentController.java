package vn.spring.webbansach_backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vn.spring.webbansach_backend.service.inter.IPaymentService;

@RestController
@RequestMapping("/payment")
public class PaymentController {
    private final IPaymentService iPaymentService;

    @Autowired
    public PaymentController(@Lazy IPaymentService iPaymentService) {
        this.iPaymentService = iPaymentService;
    }

    @GetMapping("/findPaymentByOrderId")
    public ResponseEntity<?> findPaymentByOrderId(@RequestParam("orderId") Long orderId){
        return iPaymentService.findPaymentByOrderId(orderId);
    }
}
