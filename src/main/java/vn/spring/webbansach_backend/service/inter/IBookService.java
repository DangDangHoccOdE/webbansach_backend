package vn.spring.webbansach_backend.service.inter;

import org.springframework.http.ResponseEntity;
import vn.spring.webbansach_backend.dto.BookDto;
import vn.spring.webbansach_backend.entity.Book;

public interface IBookService {
    void addBook(BookDto bookDto);
    Book findBookById(int bookId);
    ResponseEntity<?> editBook(BookDto bookDto);
    ResponseEntity<?> deleteBook(int bookId);

}
