package vn.spring.webbansach_backend.service.inter;

import org.springframework.http.ResponseEntity;
import vn.spring.webbansach_backend.dto.ReviewDto;

public interface IReviewService {
    ResponseEntity<?> addReview(Long orderId,ReviewDto reviewDto);
    ResponseEntity<?> getNumberOfStarReview(int bookId);
}
