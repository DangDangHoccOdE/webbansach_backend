package vn.spring.webbansach_backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import vn.spring.webbansach_backend.dto.CartItemDto;
import vn.spring.webbansach_backend.entity.CartItem;
import vn.spring.webbansach_backend.service.inter.ICartItemService;

import java.util.Map;
import java.util.*;

@RestController
@RequestMapping("/cart-items")
public class CartItemController {
    private final ICartItemService iCartItemService;

    @Autowired
    public CartItemController(ICartItemService iCartItemService) {
        this.iCartItemService = iCartItemService;
    }

    @GetMapping("/findCartItemsByUserId/{userId}")
    public ResponseEntity<Page<CartItem>> findCartItemsByUserId(
            @PathVariable Long userId,
            @RequestParam("page") int page,
            @RequestParam("size") int size,
            @RequestParam(name = "sort" ,defaultValue = "createdAt") String sort,
            @RequestParam(name = "direction" ,defaultValue = "desc") String direction
     ){
        Sort sorted = Sort.by(Sort.Direction.fromString(direction),sort);
        Page<CartItem> cartItems = iCartItemService.findCartItemsByUser_UserId(userId,page,size,sorted);
        return ResponseEntity.ok(cartItems);
    }

    @PostMapping("/addCartItem")
    public ResponseEntity<?> addCartItem(@Validated @RequestBody CartItemDto cartItemDto){
        return iCartItemService.addCartItem(cartItemDto);
    }

    @PutMapping("/updateQuantityOfCartItem/{cartItemId}")
    public ResponseEntity<?> updateQuantityOfCartItem(@PathVariable Long cartItemId,@RequestBody Map<String,Integer> map){
        return iCartItemService.updateQuantityOfCartItem(cartItemId,map.get("quantity"));
    }

    @DeleteMapping("/deleteCartItem/{cartItemId}")
    public ResponseEntity<?> deleteBooksOfCart(@PathVariable Long cartItemId){
        return iCartItemService.deleteCartItem(cartItemId);
    }

    @DeleteMapping("/deleteAllCartItemsIsChoose")
    public ResponseEntity<?> deleteAllCartItemsIsChoose(@RequestBody List<Long> allCartItemIsChoose){
        return iCartItemService.deleteAllCartItemsIsChoose(allCartItemIsChoose);
    }
}
