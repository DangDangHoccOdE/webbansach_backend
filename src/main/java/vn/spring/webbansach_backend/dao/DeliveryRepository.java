package vn.spring.webbansach_backend.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import vn.spring.webbansach_backend.entity.Delivery;

@RepositoryRestResource(path = "delivery")
public interface DeliveryRepository extends JpaRepository<Delivery,Integer> {
    Delivery findByDeliveryName(String deliveryName);
}

