package vn.spring.webbansach_backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.spring.webbansach_backend.service.inter.IWishListService;

@RestController
@RequestMapping("/wishList")
public class WishListController {
    @Autowired
    private IWishListService iWishListService;

    @DeleteMapping("/deleteWishList/{wishListId}")
    public ResponseEntity<?> deleteWishList(@PathVariable int wishListId){
        return iWishListService.deleteWishListById(wishListId);
    }

}
