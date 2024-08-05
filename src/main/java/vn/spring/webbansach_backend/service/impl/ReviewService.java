package vn.spring.webbansach_backend.service.impl;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import vn.spring.webbansach_backend.dao.ReviewRepository;
import vn.spring.webbansach_backend.dto.ReviewDto;
import vn.spring.webbansach_backend.entity.*;
import vn.spring.webbansach_backend.service.inter.IOrderReviewService;
import vn.spring.webbansach_backend.service.inter.IOrderService;
import vn.spring.webbansach_backend.service.inter.IReviewService;

import java.awt.geom.Arc2D;
import java.util.*;

import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ReviewService implements IReviewService {
    private final ReviewRepository reviewRepository;
    private final IOrderService iOrderService;
    private final UserService userService;
    private final IOrderReviewService iOrderReviewService;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository, IOrderService iOrderService, UserService userService, IOrderReviewService iOrderReviewService) {
        this.reviewRepository = reviewRepository;
        this.iOrderService = iOrderService;
        this.userService = userService;
        this.iOrderReviewService = iOrderReviewService;
    }

    @Override
    @Transactional
    public ResponseEntity<?> addReview(Long orderId,ReviewDto reviewDto) {
        Order order = iOrderService.findOrderById(orderId);

        if(order == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Notice("Không tìm thấy đơn hàng cần đánh giá"));
        }

        order.setOrderStatus("Đánh giá");
        iOrderService.save(order);

        // đánh giá đơn hàng
        OrderReview orderReview = new OrderReview();
        orderReview.setOrder(order);
        orderReview.setDeliveryRate(reviewDto.getDeliveryRating());
        orderReview.setShopRate(reviewDto.getShopRating());
        orderReview.setUser(order.getUser());
        iOrderReviewService.save(orderReview);

        // đánh giá sách
        Map<Integer,List<String>> getMapImagesOfBook = reviewDto.getMapImagesOfBook();
        Map<Integer,String> getMapVideoOfBook = reviewDto.getMapVideoOfBook();
        Map<Integer,String> getMapContentsOfBook = reviewDto.getMapContentsOfBook();
        Map<Integer, Float> getMapStarsOfBook = reviewDto.getMapStarsOfBook();

        order.getOrderDetailList().stream()
                .map(OrderDetail::getBook)
                .forEach(book -> {
                    Review review = new Review();
                    review.setUser(order.getUser());
                    review.setBook(book);
                    // Set video nếu có
                    Optional.ofNullable(getMapVideoOfBook.get(book.getBookId())).ifPresent(review::setVideo);

                    Optional.ofNullable(getMapStarsOfBook.get(book.getBookId())).ifPresent(review::setRate);

                    Optional.ofNullable(getMapContentsOfBook.get(book.getBookId())).ifPresent(review::setContent);
                    // Set Image nếu có
                    Optional.ofNullable(getMapImagesOfBook.get(book.getBookId())).ifPresent(image->{
                        if(image.size()>0) review.setImageOne(image.get(0));
                        if(image.size()>1) review.setImageOne(image.get(1));
                        if(image.size()>2) review.setImageOne(image.get(2));
                        if(image.size()>3) review.setImageOne(image.get(3));
                        if(image.size()>4) review.setImageOne(image.get(4));
                    });

                    reviewRepository.save(review);
                });

        return ResponseEntity.ok(new Notice("Cảm ơn bạn đã đánh giá sản phẩm"));
    }
}
