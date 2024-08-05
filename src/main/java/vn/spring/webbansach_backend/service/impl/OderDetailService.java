package vn.spring.webbansach_backend.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import vn.spring.webbansach_backend.dao.OrderDetailRepository;
import vn.spring.webbansach_backend.entity.Notice;
import vn.spring.webbansach_backend.entity.Order;
import vn.spring.webbansach_backend.entity.OrderDetail;
import vn.spring.webbansach_backend.service.inter.IOrderDetailService;
import vn.spring.webbansach_backend.service.inter.IOrderService;

import java.util.List;

@Service
public class OderDetailService implements IOrderDetailService {
    private final OrderDetailRepository orderDetailRepository;
    private final IOrderService iOrderService;

    @Autowired
    public OderDetailService(OrderDetailRepository orderDetailRepository, @Lazy IOrderService iOrderService) {
        this.orderDetailRepository = orderDetailRepository;
        this.iOrderService = iOrderService;
    }

    @Override
    public ResponseEntity<?> getOrderDetailsFromOrder(Long orderId) {
        Order order = iOrderService.findOrderById(orderId);
        if(order == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Notice("Không tìm thấy đơn hàng!"));
        }

        List<OrderDetail> orderDetails = order.getOrderDetailList();
        return ResponseEntity.ok(orderDetails);
    }
}
