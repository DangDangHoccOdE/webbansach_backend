package vn.spring.webbansach_backend.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.spring.webbansach_backend.dao.DeliveryRepository;
import vn.spring.webbansach_backend.entity.Delivery;
import vn.spring.webbansach_backend.entity.Payment;
import vn.spring.webbansach_backend.service.inter.IDeliveryService;

@Service
public class DeliveryService implements IDeliveryService {
    private final DeliveryRepository deliveryRepository;

    @Autowired
    public DeliveryService(DeliveryRepository deliveryRepository) {
        this.deliveryRepository = deliveryRepository;
    }

    @Override
    public Delivery findByDeliveryName(String deliveryName) {
        return deliveryRepository.findByDeliveryName(deliveryName);
    }

    @Override
    public void save(Delivery delivery) {
        deliveryRepository.save(delivery);
    }
}
