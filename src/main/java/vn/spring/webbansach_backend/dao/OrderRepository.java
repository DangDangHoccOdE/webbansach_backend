package vn.spring.webbansach_backend.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import vn.spring.webbansach_backend.entity.Order;

@RepositoryRestResource(path = "orders")
public interface OrderRepository extends JpaRepository<Order,Integer> {
}

