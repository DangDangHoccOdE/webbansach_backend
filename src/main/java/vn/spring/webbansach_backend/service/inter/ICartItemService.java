package vn.spring.webbansach_backend.service.inter;

import org.springframework.http.ResponseEntity;
import vn.spring.webbansach_backend.dto.CartItemDto;

public interface ICartItemService {
    ResponseEntity<?> deleteCartItem(Long cartItemId);
    ResponseEntity<?> addCartItem(CartItemDto cartItemDto);
    ResponseEntity<?> updateQuantityOfCartItem(Long cartItemId,int quantity);
}
