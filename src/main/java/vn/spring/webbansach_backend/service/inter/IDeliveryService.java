package vn.spring.webbansach_backend.service.inter;

import vn.spring.webbansach_backend.entity.Delivery;

public interface IDeliveryService {
    Delivery findByDeliveryName(String deliveryName);
}
