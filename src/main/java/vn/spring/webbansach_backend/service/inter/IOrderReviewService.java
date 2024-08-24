package vn.spring.webbansach_backend.service.inter;

import org.springframework.http.ResponseEntity;
import vn.spring.webbansach_backend.entity.OrderReview;

public interface IOrderReviewService {
    OrderReview save(OrderReview orderReview);

     ResponseEntity<?> getNumberOfStarShopReviews();
     ResponseEntity<?> getNumberOfStarDeliveryReviews();
}
