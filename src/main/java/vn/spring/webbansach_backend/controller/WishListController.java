package vn.spring.webbansach_backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import vn.spring.webbansach_backend.dto.WishListDto;
import vn.spring.webbansach_backend.service.inter.IWishListService;

import java.util.Map;

@RestController
@RequestMapping("/wishList")
public class WishListController {
    private final IWishListService iWishListService;
    @Autowired
    public WishListController(IWishListService iWishListService) {
        this.iWishListService = iWishListService;
    }

    @PostMapping("/addWishList")
    public ResponseEntity<?> addWishList(@Validated @RequestBody WishListDto wishListDto){
        return iWishListService.addWishList(wishListDto);
    }

    @PostMapping("/addBookToWishList")
    public ResponseEntity<?> addBookToWishList(@RequestBody Map<String,Integer> map){
        int wishListId = map.get("wishListId");
        int bookId = map.get("bookId");
        return iWishListService.addBookToWishList(wishListId,bookId);
    }


    @PutMapping("/editWishListName")
    public ResponseEntity<?> editWishListName(@Validated @RequestBody WishListDto wishListDto){
        return iWishListService.editWishListName(wishListDto.getWishListId(),wishListDto.getNewWishListName());
    }
    @DeleteMapping("/deleteWishList/{wishListId}")
    public ResponseEntity<?> deleteWishList(@PathVariable int wishListId){
        return iWishListService.deleteWishListById(wishListId);
    }

    @GetMapping("/showWishList/{userId}")
    public ResponseEntity<?> showWishList(@PathVariable Long userId){
        return iWishListService.showWishListByUserId(userId);
    }

    @DeleteMapping("/deleteBookOfWishList")
    public ResponseEntity<?> deleteBookOfWishList(@RequestBody Map<String,Integer> map){
        int bookId = map.get("bookId");
        int wishListId = map.get("wishListId");
        return iWishListService.deleteBookOfWishList(bookId,wishListId);
    }


}
