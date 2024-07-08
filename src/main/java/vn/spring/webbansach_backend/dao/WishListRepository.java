package vn.spring.webbansach_backend.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import vn.spring.webbansach_backend.entity.User;
import vn.spring.webbansach_backend.entity.WishList;
import java.util.*;

@RepositoryRestResource(path = "wish-list")
public interface WishListRepository extends JpaRepository<WishList,Integer> {
    WishList findByWishListId(int id);
    List<WishList> findByUser(User user);

}
