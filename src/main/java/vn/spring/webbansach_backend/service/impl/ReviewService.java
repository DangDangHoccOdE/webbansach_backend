package vn.spring.webbansach_backend.service.impl;

import jakarta.transaction.Transactional;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import vn.spring.webbansach_backend.dao.ReviewRepository;
import vn.spring.webbansach_backend.dto.ReviewDto;
import vn.spring.webbansach_backend.entity.*;
import vn.spring.webbansach_backend.service.inter.IBookService;
import vn.spring.webbansach_backend.service.inter.IOrderReviewService;
import vn.spring.webbansach_backend.service.inter.IOrderService;
import vn.spring.webbansach_backend.service.inter.IReviewService;
import vn.spring.webbansach_backend.utils.ConvertStringToDate;

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
    private final IBookService iBookService;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository, IOrderService iOrderService, UserService userService, IOrderReviewService iOrderReviewService, IBookService iBookService) {
        this.reviewRepository = reviewRepository;
        this.iOrderService = iOrderService;
        this.userService = userService;
        this.iOrderReviewService = iOrderReviewService;
        this.iBookService = iBookService;
    }

    @Override
    public ResponseEntity<?> getNumberOfStarReview(int bookId) {
        Book book = iBookService.findBookById(bookId);
        if(book == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Notice("Không tìm thấy sách"));
        }

        List<Review> reviews = book.getReviewList();
        JSONObject data = new JSONObject();

       int[] starCounts = new int[5]; // Mảng lưu trữ số lượng các đánh giá từ 1 sao đến 5 sao

       reviews.forEach(review -> {
           int rate = (int) review.getRate();
           if(rate>=1 && rate<=5){
               starCounts[rate-1]++;
           }
       });

        int oneStar = starCounts[0];
        int twoStar = starCounts[1];
        int threeStar = starCounts[2];
        int fourStar = starCounts[3];
        int fiveStar = starCounts[4];

        data.put("1",oneStar);
        data.put("2",twoStar);
        data.put("3",threeStar);
        data.put("4",fourStar);
        data.put("5",fiveStar);

        return ResponseEntity.ok(data);
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
                        if(image.size()>1) review.setImageTwo(image.get(1));
                        if(image.size()>2) review.setImageThree(image.get(2));
                        if(image.size()>3) review.setImageFour(image.get(3));
                        if(image.size()>4) review.setImageFive(image.get(4));
                    });
                    review.setOrderReview(orderReview);
                    review.setDate(ConvertStringToDate.convertToLocalDateTime(reviewDto.getDate()));
                    reviewRepository.save(review);
                });

        return ResponseEntity.ok(new Notice("Cảm ơn bạn đã đánh giá sản phẩm"));
    }
}
