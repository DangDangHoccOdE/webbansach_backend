package vn.spring.webbansach_backend.service.inter;

import org.springframework.http.ResponseEntity;
import vn.spring.webbansach_backend.dto.BookDto;
import vn.spring.webbansach_backend.entity.Book;
import java.util.*;

public interface IBookService {
    void addBook(BookDto bookDto);
    Book save(Book book);
    List<Book> saveAll(List<Book> book);
    Book findBookById(int bookId);
    ResponseEntity<?> editBook(Integer bookId,BookDto bookDto);
    ResponseEntity<?> deleteBook(Integer bookId);

}
