package vn.spring.webbansach_backend.service.inter;

import org.springframework.http.ResponseEntity;
import vn.spring.webbansach_backend.dto.CartItemDto;
import vn.spring.webbansach_backend.entity.CartItem;
import java.util.*;

public interface ICartItemService {
    CartItem saveCartItem(CartItem cartItem);
    CartItem findCartItemById(long id);
    ResponseEntity<?> deleteCartItem(Long cartItemId);
    ResponseEntity<?> addCartItem(CartItemDto cartItemDto);
    ResponseEntity<?> updateQuantityOfCartItem(Long cartItemId,int quantity);
    ResponseEntity<?> deleteAllCartItemsIsChoose(List<Long> allCartItemIsChoose);
}
