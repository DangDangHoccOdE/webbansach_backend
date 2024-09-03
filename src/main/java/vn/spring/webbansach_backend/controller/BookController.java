package vn.spring.webbansach_backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import vn.spring.webbansach_backend.dto.BookDto;
import vn.spring.webbansach_backend.entity.Book;
import vn.spring.webbansach_backend.entity.Notice;
import vn.spring.webbansach_backend.service.inter.IBookService;


@RestController
@RequestMapping("/books")
public class BookController {
    private final IBookService iBookService;
    @Autowired
    public BookController(IBookService iBookService) {
        this.iBookService = iBookService;
    }

    @GetMapping("/getBookFromWishList/{wishListId}")
    public ResponseEntity<Page<Book>> getBooksInWishList(
            @PathVariable Integer wishListId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "8") int size) {
        Page<Book> books = iBookService.getBooksInWishList(wishListId, page, size);
        return ResponseEntity.ok(books);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/addBook")
    public ResponseEntity<?> addBook(@Validated  @RequestBody BookDto bookDto){
        try{
            iBookService.addBook(bookDto);
            return ResponseEntity.ok(new Notice("Thêm thành công sách!"));
        }catch(Exception e){
            System.out.println("Lỗi thêm sách: "+e.getMessage());
            return ResponseEntity.badRequest().body(new Notice("Không thể thêm sách!"));
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/editBook/{bookId}")
    public ResponseEntity<?> editBook(@PathVariable Integer bookId,@Validated @RequestBody BookDto bookDto) {
        return iBookService.editBook(bookId,bookDto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/deleteBook/{bookId}")
    public ResponseEntity<?> deleteBook(@PathVariable int bookId) {
        return iBookService.deleteBook(bookId);
    }

}