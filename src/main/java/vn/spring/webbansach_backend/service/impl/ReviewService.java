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

import java.util.*;

import java.util.Map;

@Service
public class ReviewService implements IReviewService {
    private final ReviewRepository reviewRepository;
    private final IOrderService iOrderService;
    private final IOrderReviewService iOrderReviewService;
    private final IBookService iBookService;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository, IOrderService iOrderService, IOrderReviewService iOrderReviewService, IBookService iBookService) {
        this.reviewRepository = reviewRepository;
        this.iOrderService = iOrderService;
        this.iOrderReviewService = iOrderReviewService;
        this.iBookService = iBookService;
    }

    @Override
    public ResponseEntity<?> getNumberOfStarReview(int bookId) {
        Book book = iBookService.findBookById(bookId);
        if(book == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Notice("Không tìm thấy sách"));
        }

        JSONObject data = getNumberReviews(book);

        return ResponseEntity.ok(data);
    }

    private JSONObject getNumberReviews(Book book){ // Lấy ra số lượng đánh giá ứng với từng sao
        List<Review> reviews = book.getReviewList().stream()
                .filter(review -> !review.isHide())
                .toList();

        JSONObject data = new JSONObject();

        int[] starCounts = new int[5]; // Mảng lưu trữ số lượng các đánh giá từ 1 sao đến 5 sao
        float totalStar = 0;

        for(Review review : reviews){
            int rate = (int) review.getRate();
            if(rate>=1 && rate<=5){
                starCounts[rate-1]++;
                totalStar+=rate;
            }
        }

        int oneStar = starCounts[0];
        int twoStar = starCounts[1];
        int threeStar = starCounts[2];
        int fourStar = starCounts[3];
        int fiveStar = starCounts[4];

        int totalReview = (oneStar+twoStar+threeStar+fourStar+fiveStar);
        float averageRateNew = totalReview>0 ? totalStar/totalReview : totalStar;

        data.put("1",oneStar);
        data.put("2",twoStar);
        data.put("3",threeStar);
        data.put("4",fourStar);
        data.put("5",fiveStar);
        data.put("averageRateNew",averageRateNew);

        return data;
    }

    private void updateAverageBook(Book book){
        JSONObject starData = getNumberReviews(book);

        float rateNew = (float) starData.get("averageRateNew");

        book.setAverageRate(rateNew);

    }

    @Override
    @Transactional
    public ResponseEntity<?> addReview(Long orderId,ReviewDto reviewDto) {
        Order order = iOrderService.findOrderById(orderId);
        if(order == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Notice("Không tìm thấy đơn hàng cần đánh giá"));
        }
        if(order.getOrderReview()!=null){
            return ResponseEntity.badRequest().body(new Notice("Đánh giá đã tồn tại, không thể thên"));
        }
            addAndEditReview(order,reviewDto);
            return ResponseEntity.ok(new Notice("Cảm ơn bạn đã đánh giá sản phẩm"));
    }

    @Override
    @Transactional
    public ResponseEntity<?> editReview(Long orderId,ReviewDto reviewDto) {
        Order order = iOrderService.findOrderById(orderId);
        if(order == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Notice("Không tìm thấy đơn hàng cần đánh giá"));
        }
            addAndEditReview(order,reviewDto);
            return ResponseEntity.ok(new Notice("Cập nhật đánh giá sản phẩm thành công"));
    }

    @Override
    public void addAndEditReview(Order order, ReviewDto reviewDto) {
        order.setOrderStatus("Đánh giá");
        iOrderService.save(order);

        // đánh giá đơn hàng
        OrderReview orderReview = order.getOrderReview();
        if(orderReview == null){ // Nếu chưa tồn tại đánh giá
            orderReview = new OrderReview();
        }

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

        List<Book> booksToSave = new ArrayList<>();
        List<Review> reviewsToSave = new ArrayList<>();

        OrderReview finalOrderReview = orderReview;
        order.getOrderDetailList().stream()
                .map(OrderDetail::getBook)
                .forEach(book -> {
                            Review review = reviewRepository.findByOrderReview_OrderReviewIdAndBook_BookId(finalOrderReview.getOrderReviewId(), book.getBookId());
                            if (review == null) {
                                review = new Review();
                            }
                            review.setUser(order.getUser());
                            review.setBook(book);
                            // Set video nếu có
                            Optional.ofNullable(getMapVideoOfBook.get(book.getBookId())).ifPresent(review::setVideo);

                            Optional.ofNullable(getMapStarsOfBook.get(book.getBookId())).ifPresent(review::setRate);

                            Optional.ofNullable(getMapContentsOfBook.get(book.getBookId())).ifPresent(review::setContent);
                            // Set Image nếu có
                            Review finalReview = review;
                            review.setImageOne(null); // Sét tất cả ảnh thành null
                            review.setImageTwo(null);
                            review.setImageThree(null);
                            review.setImageFour(null);
                            review.setImageFive(null);

                            Optional.ofNullable(getMapImagesOfBook.get(book.getBookId())).ifPresent(image -> {
                                if (!image.isEmpty()) finalReview.setImageOne(image.get(0));
                                if (image.size() > 1) finalReview.setImageTwo(image.get(1));
                                if (image.size() > 2) finalReview.setImageThree(image.get(2));
                                if (image.size() > 3) finalReview.setImageFour(image.get(3));
                                if (image.size() > 4) finalReview.setImageFive(image.get(4));
                            });
                            review.setOrderReview(finalOrderReview);
                            review.setDate(ConvertStringToDate.convertToLocalDateTime(reviewDto.getDate()));
                            reviewsToSave.add(finalReview);
                             booksToSave.add(book);
                });
        reviewRepository.saveAll(reviewsToSave);

        for(Book book : booksToSave){
            // Cập nhật lại số sao trung bình của cuốn sách
            updateAverageBook(book);
        }
        iBookService.saveAll(booksToSave);
    }

    @Override
    @Transactional
    public ResponseEntity<?> hideReview(Long reviewId) {
        Review review = reviewRepository.findByReviewId(reviewId);
        if (review == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Notice("Không tìm thấy đánh giá!"));
        }

        if (review.isHide()) {
            return ResponseEntity.badRequest().body(new Notice("Đánh giá đã bị ẩn!"));
        }

            Book book = review.getBook();
            review.setHide(true);
            reviewRepository.save(review);

            // Cập nhật lại số sao trung bình của cuốn sách
            updateAverageBook(book);
            iBookService.save(book);

            return ResponseEntity.ok(new Notice("Đã ẩn đánh giá thành công!"));

    }
}
