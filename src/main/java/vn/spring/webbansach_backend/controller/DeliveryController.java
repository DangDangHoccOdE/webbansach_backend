package vn.spring.webbansach_backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vn.spring.webbansach_backend.service.inter.IDeliveryService;

@RestController
@RequestMapping("/delivery")
public class DeliveryController {
    private final IDeliveryService iDeliveryService;

    @Autowired
    public DeliveryController(@Lazy IDeliveryService iDeliveryService) {
        this.iDeliveryService = iDeliveryService;
    }

    @GetMapping("/findDeliveryByOrderId")
    public ResponseEntity<?> findDeliveryByOrderId(@RequestParam("orderId") Long orderId) {
        return iDeliveryService.findDeliveryByOrderId(orderId);
    }
}
