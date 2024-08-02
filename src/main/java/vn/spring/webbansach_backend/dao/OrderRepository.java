package vn.spring.webbansach_backend.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.RequestParam;
import vn.spring.webbansach_backend.entity.Order;
import java.util.*;
@RepositoryRestResource(path = "orders")
public interface OrderRepository extends JpaRepository<Order,Integer> {
    Order findByUser_UserId(Long userId);
    Order findByOrderId(Long orderId);
    List<Order> findByUser_UserIdAndOrderStatusContaining(@RequestParam("userId") Long userId,@RequestParam("orderStatus") String orderStatus);
}

