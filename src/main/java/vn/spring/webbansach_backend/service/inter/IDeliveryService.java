package vn.spring.webbansach_backend.service.inter;

import org.springframework.http.ResponseEntity;
import vn.spring.webbansach_backend.entity.Delivery;

public interface IDeliveryService {
    Delivery findByDeliveryName(String deliveryName);
    void save(Delivery delivery);

    ResponseEntity<?> findDeliveryByOrderId(Long orderId);


}
