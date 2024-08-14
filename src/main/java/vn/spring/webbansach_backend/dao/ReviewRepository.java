package vn.spring.webbansach_backend.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.RequestParam;
import vn.spring.webbansach_backend.entity.Review;

@RepositoryRestResource(path = "reviews")
public interface ReviewRepository extends JpaRepository<Review,Long> {
    Page<Review> findByRateAndBook_BookId(@RequestParam("rate") Float rate, @RequestParam("bookId") Integer bookId,Pageable pageable);
    Page<Review> findByBook_BookId(@RequestParam("bookId") Integer bookId,Pageable pageable);

    Review findByOrderReview_OrderReviewIdAndBook_BookId(Long orderReview_orderReviewId, Integer book_bookId);

    Review findByReviewId(Long reviewId);
}

