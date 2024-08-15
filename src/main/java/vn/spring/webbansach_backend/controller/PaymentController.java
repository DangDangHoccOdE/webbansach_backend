package vn.spring.webbansach_backend.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.spring.webbansach_backend.dto.OrderDto;
import vn.spring.webbansach_backend.dto.TransactionDto;
import vn.spring.webbansach_backend.service.impl.VNPayService;
import vn.spring.webbansach_backend.service.inter.IPaymentService;

import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping("/payment")
public class PaymentController {
    private final IPaymentService iPaymentService;
    private final VNPayService vnPayService;

    @Autowired
    public PaymentController(@Lazy IPaymentService iPaymentService, VNPayService vnPayService) {
        this.iPaymentService = iPaymentService;
        this.vnPayService = vnPayService;
    }

    @GetMapping("/findPaymentByOrderId")
    public ResponseEntity<?> findPaymentByOrderId(@RequestParam("orderId") Long orderId){
        return iPaymentService.findPaymentByOrderId(orderId);
    }

    @PostMapping("/create-payment")
    public ResponseEntity<?> createPayment(HttpServletRequest request,@RequestParam("amount") long amount,@RequestParam("orderInfo") String orderInfo) throws UnsupportedEncodingException {
        String vNPayUrl = vnPayService.createOrder(amount,orderInfo,request); // orderInfo là mã đơn hàng
        return ResponseEntity.ok().body(vNPayUrl);
    }

}
