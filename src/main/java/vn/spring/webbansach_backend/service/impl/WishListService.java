package vn.spring.webbansach_backend.service.impl;

import java.util.*;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import vn.spring.webbansach_backend.dao.WishListRepository;
import vn.spring.webbansach_backend.entity.Notice;
import vn.spring.webbansach_backend.entity.User;
import vn.spring.webbansach_backend.entity.WishList;
import vn.spring.webbansach_backend.service.inter.IUserService;
import vn.spring.webbansach_backend.service.inter.IWishListService;

@Service
public class WishListService implements IWishListService {
    @Autowired
    private WishListRepository wishListRepository;
    @Autowired
    private IUserService iUserService;

    @Override
    public ResponseEntity<?> deleteWishListById(int wishListId) {
        WishList wishList = wishListRepository.findByWishListId(wishListId);
        if(wishList==null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Notice("Không tìm thấy danh sách yêu thích!"));
        }
        wishListRepository.delete(wishList);
        return ResponseEntity.ok(new Notice("Đã xóa thành công!"));
    }

    @Override
    public ResponseEntity<?> showWishListByUserId(Long userId) {
        User user = iUserService.findUserByUserId(userId);

        if(user == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Notice("Không tìm thấy người sử dụng!"));
        }

        List<WishList> findWishListByUser = wishListRepository.findByUser(user);
        List<Integer> idWishList = new ArrayList<>();

        for(WishList wishList : findWishListByUser){
            idWishList.add(wishList.getWishListId());
        }
        return ResponseEntity.ok().body(idWishList);
    }
}
