package vn.spring.webbansach_backend.service.inter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import vn.spring.webbansach_backend.dao.WishListRepository;

public interface IWishListService {
    ResponseEntity<?> deleteWishListById(int wishListId);
}
