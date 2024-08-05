package vn.spring.webbansach_backend.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import vn.spring.webbansach_backend.entity.OrderReview;

@RepositoryRestResource(path = "order-review")
public interface OrderReviewRepository extends JpaRepository<OrderReview,Long> {

}
