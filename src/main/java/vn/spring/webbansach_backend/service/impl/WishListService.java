package vn.spring.webbansach_backend.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import vn.spring.webbansach_backend.dao.WishListRepository;
import vn.spring.webbansach_backend.entity.Notice;
import vn.spring.webbansach_backend.entity.WishList;
import vn.spring.webbansach_backend.service.inter.IWishListService;

@Service
public class WishListService implements IWishListService {
    @Autowired
    private WishListRepository wishListRepository;

    @Override
    public ResponseEntity<?> deleteWishListById(int wishListId) {
        WishList wishList = wishListRepository.findByWishListId(wishListId);
        if(wishList==null){
            return ResponseEntity.badRequest().body(new Notice("Danh sách yêu thích không tồn tại!"));
        }
        wishListRepository.delete(wishList);
        return ResponseEntity.ok(new Notice("Đã xóa thành công!"));
    }
}
