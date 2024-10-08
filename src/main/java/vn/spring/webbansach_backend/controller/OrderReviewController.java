package vn.spring.webbansach_backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.spring.webbansach_backend.service.impl.OrderReviewService;

@RequestMapping("/order-review")
@RestController
public class OrderReviewController {
    private final OrderReviewService orderReviewService;

    @Autowired
    public OrderReviewController(OrderReviewService orderReviewService) {
        this.orderReviewService = orderReviewService;
    }

    @GetMapping("/findTotalReviewShop")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> findTotalReviewShop(){
        return orderReviewService.getNumberOfStarShopReviews();
    }

    @GetMapping("/findTotalReviewDelivery")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> findTotalReviewDelivery(){
        return orderReviewService.getNumberOfStarDeliveryReviews();
    }
}
