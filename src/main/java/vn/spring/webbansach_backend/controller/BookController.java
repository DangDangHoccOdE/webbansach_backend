package vn.spring.webbansach_backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import vn.spring.webbansach_backend.dto.BookDto;
import vn.spring.webbansach_backend.entity.Notice;
import vn.spring.webbansach_backend.service.impl.BookService;


@RestController
@RequestMapping("/admin")
public class BookController {
    private final BookService bookService;
    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/addBook")
    public ResponseEntity<?> addBook(@Validated  @RequestBody BookDto bookDto){
        try{
            bookService.addBook(bookDto);
            return ResponseEntity.ok(new Notice("Thêm thành công sách!"));
        }catch(Exception e){
            System.out.println("Lỗi thêm sách: "+e.getMessage());
            return ResponseEntity.badRequest().body(new Notice("Không thể thêm sách!"));
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/editBook")
    public ResponseEntity<?> editBook(@Validated @RequestBody BookDto bookDto) {
            return bookService.editBook(bookDto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/deleteBook/{bookId}")
    public ResponseEntity<?> deleteBook(@PathVariable int bookId) {
        return bookService.deleteBook(bookId);
    }

}
