package vn.spring.webbansach_backend.service.inter;

import vn.spring.webbansach_backend.entity.Delivery;
import vn.spring.webbansach_backend.entity.Payment;

public interface IDeliveryService {
    Delivery findByDeliveryName(String deliveryName);
    void save(Delivery delivery);

}
