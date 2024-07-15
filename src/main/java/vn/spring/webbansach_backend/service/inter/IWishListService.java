package vn.spring.webbansach_backend.service.inter;

import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import vn.spring.webbansach_backend.dao.WishListRepository;
import vn.spring.webbansach_backend.dto.WishListDto;
import vn.spring.webbansach_backend.entity.WishList;

public interface IWishListService {
    void saveWishList(WishList wishList);
    ResponseEntity<?> deleteWishListById(int wishListId);
    ResponseEntity<?> addWishList(WishListDto wishListDto);
    ResponseEntity<?> addBookToWishList(int wishListId, int bookId);
    ResponseEntity<JSONObject> showWishListByUserId(Long userId);
    ResponseEntity<?> editWishListName(int wishListId,String wishListName);
    ResponseEntity<?> deleteBookOfWishList(int bookId,int wishListId);
}
