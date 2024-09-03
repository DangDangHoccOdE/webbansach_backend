package vn.spring.webbansach_backend.service.inter;

import org.springframework.http.ResponseEntity;
import vn.spring.webbansach_backend.dto.ReviewDto;
import vn.spring.webbansach_backend.entity.Order;

public interface IReviewService {
    ResponseEntity<?> addReview(Long orderId,ReviewDto reviewDto);
    ResponseEntity<?> editReview(Long orderId,ReviewDto reviewDto);
    ResponseEntity<?> hideReview(Long reviewId);
    ResponseEntity<?> getNumberOfStarReview(int bookId);
    void addAndEditReview(Order order, ReviewDto reviewDto);
}
