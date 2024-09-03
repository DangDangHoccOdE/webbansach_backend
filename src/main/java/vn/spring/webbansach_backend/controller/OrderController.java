package vn.spring.webbansach_backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import vn.spring.webbansach_backend.dto.OrderDto;
import vn.spring.webbansach_backend.service.impl.OrderService;
import vn.spring.webbansach_backend.service.inter.IOrderService;

import java.util.Map;

@RestController
@RequestMapping("/order")
public class OrderController {
    private final IOrderService iOrderService;

    @Autowired
    public OrderController(OrderService iOrderService) {
        this.iOrderService = iOrderService;
    }


    @GetMapping("/getBooksOfOrder/{orderId}")
    public ResponseEntity<?> getBooksOfOrder(@PathVariable Long orderId){
        return iOrderService.getBooksOfOrder(orderId);
    }

    @PostMapping("/addOrder")
    public ResponseEntity<?> addOrder(@Validated @RequestBody OrderDto orderDto,@RequestParam boolean isBuyNow){
        return iOrderService.addOrder(orderDto,isBuyNow);
    }
    @PutMapping("/cancelOrder/{orderId}")
    public ResponseEntity<?> cancelOrder(@PathVariable Long orderId,@RequestBody Map<String,String> mapReason){
        return iOrderService.cancelOder(orderId,mapReason);
    }
    @PutMapping("/confirmReceivedOrder/{orderId}")
    public ResponseEntity<?> confirmReceivedOrder(@PathVariable Long orderId){
        return iOrderService.confirmReceivedOrder(orderId);
    }

    @PutMapping("/confirmSuccessfullyBankOrderPayment/{orderCode}")
    public ResponseEntity<?> confirmSuccessfullyBankOrderPayment(@PathVariable String orderCode){
        return iOrderService.confirmSuccessfullyBankOrderPayment(orderCode);
    }

    @PutMapping("/saveOrderStatusChange/{orderId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> saveOrderStatusChange(@PathVariable long orderId,@RequestBody OrderDto orderDto){
        return iOrderService.saveOrderStatusChange(orderId,orderDto);
    }

    @PutMapping("/repurchase/{orderId}")
    public ResponseEntity<?> repurchase(@PathVariable Long orderId){
        return iOrderService.repurchase(orderId);
    }
}
