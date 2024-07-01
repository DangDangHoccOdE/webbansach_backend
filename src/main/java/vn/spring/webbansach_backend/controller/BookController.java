package vn.spring.webbansach_backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import vn.spring.webbansach_backend.dto.BookDto;
import vn.spring.webbansach_backend.entity.Notice;
import vn.spring.webbansach_backend.service.impl.BookService;


@RestController
@RequestMapping("/admin")
public class BookController {
    @Autowired
    private BookService bookService;
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

    @PutMapping("/editBook")
    public ResponseEntity<?> editBook(@Validated @RequestBody BookDto bookDto) {
            ResponseEntity<?> response = bookService.editBook(bookDto);
            return response;
    }

}
