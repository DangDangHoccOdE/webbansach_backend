package vn.spring.webbansach_backend.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.spring.webbansach_backend.dao.OrderReviewRepository;
import vn.spring.webbansach_backend.entity.OrderReview;
import vn.spring.webbansach_backend.service.inter.IOrderReviewService;

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
}
