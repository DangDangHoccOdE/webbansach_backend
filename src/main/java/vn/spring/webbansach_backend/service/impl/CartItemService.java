package vn.spring.webbansach_backend.service.impl;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import vn.spring.webbansach_backend.dao.CartItemRepository;
import vn.spring.webbansach_backend.dto.CartItemDto;
import vn.spring.webbansach_backend.entity.Book;
import vn.spring.webbansach_backend.entity.CartItem;
import vn.spring.webbansach_backend.entity.Notice;
import vn.spring.webbansach_backend.entity.User;
import vn.spring.webbansach_backend.service.inter.IBookService;
import vn.spring.webbansach_backend.service.inter.ICartItemService;
import vn.spring.webbansach_backend.service.inter.IUserService;

import java.time.LocalDateTime;

@Service
public class CartItemService implements ICartItemService {
    private final CartItemRepository cartItemRepository;
    private final IUserService iUserService;
    private final IBookService iBookService;

    @Autowired
    public CartItemService(CartItemRepository cartItemRepository,IBookService iBookService,IUserService iUserService) {
        this.cartItemRepository = cartItemRepository;
        this.iBookService = iBookService;
        this.iUserService= iUserService;
    }

    @Override
    @Transactional
    public ResponseEntity<?> addCartItem(CartItemDto cartItemDto) {
        User user= iUserService.findUserByUserId(cartItemDto.getUserId());
        Book book = iBookService.findBookById(cartItemDto.getBookId());

        if(user == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Notice("Không tìm thấy người sử dung!"));
        }
        if(book == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Notice("Không tìm thấy sách!"));
        }

        CartItem cartItemExist = cartItemRepository.findByBooks_BookIdAndUser_UserId(book.getBookId(),user.getUserId());
        if(cartItemExist!=null){
            cartItemExist.setQuantity(cartItemExist.getQuantity()+cartItemDto.getQuantity());
            cartItemExist.setCreatedAt(LocalDateTime.now());
            cartItemRepository.save(cartItemExist);
        }else {
            CartItem cartItem = new CartItem();
            cartItem.setUser(user);
            cartItem.setQuantity(cartItemDto.getQuantity());
            cartItem.setBooks(book);
            cartItem.setCreatedAt(LocalDateTime.now());
            cartItemRepository.save(cartItem);
        }
        return ResponseEntity.ok(new Notice("Thêm sản phẩm thành công!"));
    }

    @Override
    @Transactional
    public ResponseEntity<?> deleteCartItem(Long cartItemId) {
        CartItem cartItem = cartItemRepository.findByCartItemId(cartItemId);
        if(cartItem == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Notice("Không tìm thấy sản phẩm cần xóa"));
        }
        cartItemRepository.delete(cartItem);
        return ResponseEntity.ok(new Notice("Đã xóa thành công"));
    }
}
