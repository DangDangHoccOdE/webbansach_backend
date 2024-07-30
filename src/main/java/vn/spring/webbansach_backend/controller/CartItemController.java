package vn.spring.webbansach_backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import vn.spring.webbansach_backend.dto.CartItemDto;
import vn.spring.webbansach_backend.service.inter.ICartItemService;

import java.util.Map;

@RestController
@RequestMapping("/cart-items")
public class CartItemController {
    private final ICartItemService iCartItemService;

    @Autowired
    public CartItemController(ICartItemService iCartItemService) {
        this.iCartItemService = iCartItemService;
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
}
