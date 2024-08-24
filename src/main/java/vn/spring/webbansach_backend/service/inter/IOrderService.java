package vn.spring.webbansach_backend.service.inter;

import org.springframework.http.ResponseEntity;
import vn.spring.webbansach_backend.dto.OrderDto;
import vn.spring.webbansach_backend.entity.Order;

import java.util.Date;
import java.util.Map;

public interface IOrderService {
    Order findOrderById(Long orderId);
    Order save(Order order);
    ResponseEntity<?> addOrder(OrderDto orderDto,boolean isBuyNow);
    ResponseEntity<?> cancelOder(Long orderId, Map<String,String> mapReason);
    ResponseEntity<?> repurchase(Long orderId);
    ResponseEntity<?> confirmReceivedOrder(Long orderId);
    ResponseEntity<?> confirmSuccessfullyBankOrderPayment(String orderCode);
    ResponseEntity<?> saveOrderStatusChange(long orderId,OrderDto orderDto);
    ResponseEntity<?> getBooksOfOrder(Long orderId);

}
