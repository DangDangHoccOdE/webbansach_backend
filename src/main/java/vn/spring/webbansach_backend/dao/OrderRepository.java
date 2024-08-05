package vn.spring.webbansach_backend.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.RequestParam;
import vn.spring.webbansach_backend.entity.Order;

@RepositoryRestResource(path = "orders")
public interface OrderRepository extends JpaRepository<Order,Integer> {
    Order findByOrderId(Long orderId);
    Page<Order> findByUser_UserIdAndOrderStatusContaining(@RequestParam("userId") Long userId, @RequestParam("orderStatus") String orderStatus, Pageable pageable);
    Page<Order> findByUser_UserId(@RequestParam("userId") Long userId, Pageable pageable);
}

