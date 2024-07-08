package vn.spring.webbansach_backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.spring.webbansach_backend.annotation.UserCheckAuthorize;
import vn.spring.webbansach_backend.dto.WishListDto;
import vn.spring.webbansach_backend.service.MySecurityService;
import vn.spring.webbansach_backend.service.inter.IWishListService;

@RestController
@RequestMapping("/wishList")
public class WishListController {
    @Autowired
    private IWishListService iWishListService;
    @Autowired
    private MySecurityService mySecurityService;
    @PostMapping("/addWishList")
    public ResponseEntity<?> addWishList(@RequestBody WishListDto wishListDto){
        System.out.println("Body: "+wishListDto);
        return iWishListService.addWishList(wishListDto);
    }

    @DeleteMapping("/deleteWishList/{wishListId}")
    public ResponseEntity<?> deleteWishList(@PathVariable int wishListId){
        return iWishListService.deleteWishListById(wishListId);
    }

    @GetMapping("/showWishList/{userId}")
    public ResponseEntity<?> showWishList(@PathVariable Long userId){
        return iWishListService.showWishListByUserId(userId);
    }

}
