package vn.spring.webbansach_backend.service.impl;

import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import vn.spring.webbansach_backend.dao.OrderReviewRepository;
import vn.spring.webbansach_backend.entity.OrderReview;
import vn.spring.webbansach_backend.service.inter.IOrderReviewService;
import java.util.*;

@Service
public class OrderReviewService implements IOrderReviewService {
    private final OrderReviewRepository orderReviewRepository;

    @Autowired
    public OrderReviewService(OrderReviewRepository orderReviewRepository) {
        this.orderReviewRepository = orderReviewRepository;
    }

    @Override
    public OrderReview save(OrderReview orderReview) {
        return orderReviewRepository.save(orderReview);
    }

    @Override
    public ResponseEntity<?> getNumberOfStarShopReviews() {
        return getStarReviews(true);
    }

    @Override
    public ResponseEntity<?> getNumberOfStarDeliveryReviews() {
        return getStarReviews(false);
    }

    private ResponseEntity<?> getStarReviews(boolean isShopReview) {
        List<OrderReview> orderReviewList = orderReviewRepository.findAll();
        JSONObject data = new JSONObject();

        int[] starCounts = new int[5];
        float totalStar = 0;

        for (OrderReview orderReview : orderReviewList) {
            int rate = (int) (isShopReview ? orderReview.getShopRate() : orderReview.getDeliveryRate());
            if (rate >= 1 && rate <= 5) {
                starCounts[rate - 1]++;
                totalStar += rate;
            }
        }

        int totalReview = 0;
        for (int i = 0; i < 5; i++) {
            data.put(String.valueOf(i + 1), starCounts[i]);
            totalReview += starCounts[i];
        }

        float averageRate = totalReview > 0 ? totalStar / totalReview : 0;
        String averageRateKey = isShopReview ? "averageRateShop" : "averageRateDelivery";
        data.put(averageRateKey, averageRate);

        return ResponseEntity.ok(data);
    }
}