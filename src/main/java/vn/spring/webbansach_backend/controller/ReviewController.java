package vn.spring.webbansach_backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import vn.spring.webbansach_backend.dto.ReviewDto;
import vn.spring.webbansach_backend.service.inter.IReviewService;

@RequestMapping("/review")
@RestController
public class ReviewController {
    private final IReviewService iReviewService;

    @Autowired
    public ReviewController(IReviewService iReviewService) {
        this.iReviewService = iReviewService;
    }

    @GetMapping("/getNumberOfStarReview/{bookId}")
    public ResponseEntity<?> getNumberOfStarReview(@PathVariable("bookId") int bookId){
        return iReviewService.getNumberOfStarReview(bookId);

    }

    @PostMapping("/addReviewOrder/{orderId}")
    public ResponseEntity<?> addReview(@PathVariable Long orderId, @Validated @RequestBody ReviewDto reviewDto){
        return iReviewService.addReview(orderId,reviewDto);
    }
}
