package vn.spring.webbansach_backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import vn.spring.webbansach_backend.dto.CartItemDto;
import vn.spring.webbansach_backend.service.inter.ICartItemService;

@RestController
@RequestMapping("/cart-items")
public class CartItemController {
    private final ICartItemService iCartItemService;

    @Autowired
    public CartItemController(ICartItemService iCartItemService) {
        this.iCartItemService = iCartItemService;
    }

    @PostMapping()
    public ResponseEntity<?> addCartItem(@Validated @RequestBody CartItemDto cartItemDto){
        return iCartItemService.addCartItem(cartItemDto);
    }

    @DeleteMapping("/{cartItemId}")
    public ResponseEntity<?> deleteBooksOfCart(@PathVariable long cartItemId){
        return iCartItemService.deleteCartItem(cartItemId);
    }
}
