package vn.spring.webbansach_backend.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import vn.spring.webbansach_backend.dao.DeliveryRepository;
import vn.spring.webbansach_backend.dto.DeliveryDto;
import vn.spring.webbansach_backend.entity.Delivery;
import vn.spring.webbansach_backend.entity.Notice;
import vn.spring.webbansach_backend.entity.Order;

import vn.spring.webbansach_backend.service.inter.IDeliveryService;
import vn.spring.webbansach_backend.service.inter.IOrderService;

@Service
public class DeliveryService implements IDeliveryService {
    private final DeliveryRepository deliveryRepository;
    private final IOrderService iOrderService;

    @Autowired
    public DeliveryService(DeliveryRepository deliveryRepository, IOrderService iOrderService) {
        this.deliveryRepository = deliveryRepository;
        this.iOrderService = iOrderService;
    }

    @Override
    public Delivery findByDeliveryName(String deliveryName) {
        return deliveryRepository.findByDeliveryName(deliveryName);
    }

    @Override
    public void save(Delivery delivery) {
        deliveryRepository.save(delivery);
    }

    @Override
    public ResponseEntity<?> findDeliveryByOrderId(Long orderId) {
        Order order = iOrderService.findOrderById(orderId);

        if(order == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Notice("Không tìm thấy đơn hàng!"));
        }

        DeliveryDto deliveryDto = new DeliveryDto();
        deliveryDto.setDeliveryName(order.getDelivery().getDeliveryName());
        deliveryDto.setShippingFee(order.getDelivery().getShippingFee());
        deliveryDto.setDeliveryId(order.getDelivery().getDeliveryId());

        return ResponseEntity.ok().body(deliveryDto);
    }
}
