package vn.spring.webbansach_backend.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import vn.spring.webbansach_backend.entity.CartItem;

@RepositoryRestResource(path = "cart-items")
public interface CartItemRepository extends JpaRepository<CartItem,Long> {
    CartItem findByCartItemId(Long cartItemId);
}
