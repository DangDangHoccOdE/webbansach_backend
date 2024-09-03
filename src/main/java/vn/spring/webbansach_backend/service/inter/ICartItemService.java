package vn.spring.webbansach_backend.service.inter;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import vn.spring.webbansach_backend.dto.CartItemDto;
import vn.spring.webbansach_backend.entity.CartItem;
import java.util.*;

public interface ICartItemService {
    CartItem saveCartItem(CartItem cartItem);
    CartItem findByUserIdAndBookId(Long userId, int bookId);
    CartItem findCartItemById(long id);
    ResponseEntity<?> deleteCartItem(Long cartItemId);
    ResponseEntity<?> addCartItem(CartItemDto cartItemDto);
    ResponseEntity<?> updateQuantityOfCartItem(Long cartItemId,int quantity);
    ResponseEntity<?> deleteAllCartItemsIsChoose(List<Long> allCartItemIsChoose);
    Page<CartItem> findCartItemsByUser_UserId(Long userId, int page, int size, Sort sort);
}
