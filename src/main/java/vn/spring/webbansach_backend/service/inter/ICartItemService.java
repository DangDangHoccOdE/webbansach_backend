package vn.spring.webbansach_backend.service.inter;

import org.springframework.http.ResponseEntity;
import vn.spring.webbansach_backend.dto.CartItemDto;
import vn.spring.webbansach_backend.entity.CartItem;

public interface ICartItemService {
    CartItem findCartItemById(long id);
    ResponseEntity<?> deleteCartItem(Long cartItemId);
    ResponseEntity<?> addCartItem(CartItemDto cartItemDto);
    ResponseEntity<?> updateQuantityOfCartItem(Long cartItemId,int quantity);
}
