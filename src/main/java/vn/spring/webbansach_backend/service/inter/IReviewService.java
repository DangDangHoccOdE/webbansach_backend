package vn.spring.webbansach_backend.service.inter;

import org.springframework.http.ResponseEntity;
import vn.spring.webbansach_backend.dto.ReviewDto;
import vn.spring.webbansach_backend.entity.Order;
import vn.spring.webbansach_backend.entity.Review;

public interface IReviewService {
    ResponseEntity<?> addReview(Long orderId,ReviewDto reviewDto);
    ResponseEntity<?> editReview(Long orderId,ReviewDto reviewDto);
    ResponseEntity<?> getNumberOfStarReview(int bookId);
    void addAndEditReview(Order order, ReviewDto reviewDto);
}
