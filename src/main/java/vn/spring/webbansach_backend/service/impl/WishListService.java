package vn.spring.webbansach_backend.service.impl;

import java.util.*;

import jakarta.transaction.Transactional;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import vn.spring.webbansach_backend.dao.WishListRepository;
import vn.spring.webbansach_backend.dto.WishListDto;
import vn.spring.webbansach_backend.entity.Book;
import vn.spring.webbansach_backend.entity.Notice;
import vn.spring.webbansach_backend.entity.User;
import vn.spring.webbansach_backend.entity.WishList;
import vn.spring.webbansach_backend.service.inter.IBookService;
import vn.spring.webbansach_backend.service.inter.IUserService;
import vn.spring.webbansach_backend.service.inter.IWishListService;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class WishListService implements IWishListService {
    @Autowired
    private WishListRepository wishListRepository;
    @Autowired
    private IUserService iUserService;
    @Autowired
    private IBookService iBookService;

    @Override
    @Transactional
    public ResponseEntity<?> addWishList(WishListDto wishListDto) {
        User user = iUserService.findUserByUserId(wishListDto.getUserId());
        if(user == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Notice("Không tìm thấy người sử dụng!"));
        }
        if(wishListRepository.existsByUser_UserIdAndWishListName(wishListDto.getUserId(),wishListDto.getNewWishListName())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Notice("Tên danh sách yêu thích đã tồn tại!"));
        }

        WishList wishList = new WishList();
        wishList.setWishListName(wishListDto.getNewWishListName());
        wishList.setUser(user);
        wishListRepository.save(wishList);
        return ResponseEntity.ok(new Notice("Đã lưu thành công!"));
    }

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
    public ResponseEntity<JSONObject> showWishListByUserId(Long userId) {
        User user = iUserService.findUserByUserId(userId);
        JSONObject data = new JSONObject();
        if(user == null){
            data.put("notice","Không tìm thấy người sử dụng!");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(data);
        }

        List<WishList> findWishListByUser = wishListRepository.findByUser(user);
        data.put("wishLists",findWishListByUser);

        return ResponseEntity.ok().body(data);
    }

    @Override
    public ResponseEntity<?> addBookToWishList(int wishListId, int bookId) {
        Book book = iBookService.findBookById(bookId);
        WishList wishList = wishListRepository.findByWishListId(wishListId);

        if(book==null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Notice("Sách không tồn tại!"));
        }

        if(wishList==null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Notice("Danh sách yêu thích không tồn tại!"));
        }

        if(wishList.getBookList().contains(book)){
            return ResponseEntity.badRequest().body(new Notice("Sách đã nằm trong danh sách yêu thích này!"));
        }
        wishList.getBookList().add(book);
        wishList.setQuantity(wishList.getQuantity()+1);
        wishListRepository.save(wishList);
        return ResponseEntity.ok(new Notice("Thêm vào danh sách yêu thích thành công!"));
    }

    @Override
    @Transactional
    public ResponseEntity<?> editWishListName(int wishListId, String newWishListName) {
        WishList wishList = wishListRepository.findByWishListId(wishListId);
        if(wishList ==null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Notice("Danh sách yêu thích không tồn tại!"));
        }

        if(wishList.getWishListName().equals(newWishListName)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Notice("Tên danh sách yêu thích mới không thể trùng!"));
        }
        wishList.setWishListName(newWishListName);
        wishListRepository.save(wishList);
        return ResponseEntity.ok(new Notice("Đã đổi tên thành công"));
    }

    @Override
    @Transactional
    public ResponseEntity<?> deleteBookOfWishList(int bookId, int wishListId) {
        Book book = iBookService.findBookById(bookId);
        WishList wishList = wishListRepository.findByWishListId(wishListId);

        if(book == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Notice("Sách không tồn tại!"));
        }
        if(wishList ==null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Notice("Danh sách yêu thích không tồn tại!"));
        }

        List<Book> bookList = wishList.getBookList();
        if(!bookList.contains(book)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Notice("Sách không thuộc danh sách yêu thích này!!"));
        }

        List<Book> bookList1 = bookList.stream().filter(book1 -> book1.getBookId()!=book.getBookId()).collect(Collectors.toList());
        wishList.setBookList(bookList1);
        wishList.setQuantity(wishList.getQuantity()-1);
        wishListRepository.save(wishList);
        return ResponseEntity.ok(new Notice("Đã xóa thành công!"));
    }
}
