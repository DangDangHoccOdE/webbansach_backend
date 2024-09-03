package vn.spring.webbansach_backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import vn.spring.webbansach_backend.service.inter.IOrderDetailService;

@RestController
@RequestMapping("/order-detail")
public class OrderDetailController {
    private final IOrderDetailService iOrderDetailService;

    @Autowired
    public OrderDetailController(IOrderDetailService iOrderDetailService) {
        this.iOrderDetailService = iOrderDetailService;
    }

    @GetMapping("/getOrderDetailsFromOrderId/{orderId}")
    public ResponseEntity<?> getOrderDetailsFromOrderId(@PathVariable Long orderId){
        return iOrderDetailService.getOrderDetailsFromOrder(orderId);
    }

}
