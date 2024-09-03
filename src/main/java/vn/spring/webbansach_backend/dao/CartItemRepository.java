package vn.spring.webbansach_backend.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.RequestParam;
import vn.spring.webbansach_backend.entity.CartItem;

@RepositoryRestResource(path = "cart-items")
public interface CartItemRepository extends JpaRepository<CartItem,Long> {
    CartItem findByCartItemId(Long cartItemId);
    CartItem findByBooks_BookIdAndUser_UserId(Integer bookId,Long userId);

    @Query("select c from CartItem c join c.user u where u.userId=:userId")
    Page<CartItem> findCartItemsByUser_UserId(@RequestParam("userId") Long userId, Pageable pageable);
}
