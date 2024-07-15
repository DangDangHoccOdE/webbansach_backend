package vn.spring.webbansach_backend.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import vn.spring.webbansach_backend.entity.Cart;

@RepositoryRestResource(path = "carts")
public interface CartRepository extends JpaRepository<Cart,Long> {

}
