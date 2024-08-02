package vn.spring.webbansach_backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import vn.spring.webbansach_backend.dto.OrderDto;
import vn.spring.webbansach_backend.service.impl.OrderService;
import vn.spring.webbansach_backend.service.inter.IOrderService;

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
    public ResponseEntity<?> addOrder(@Validated @RequestBody OrderDto orderDto){
        return iOrderService.addOrder(orderDto);
    }
    @PutMapping("/cancelOrder/{orderId}")
    public ResponseEntity<?> cancelOrder(@PathVariable Long orderId){
        return iOrderService.cancelOder(orderId);
    }
}