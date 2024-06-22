package vn.spring.webbansach_backend.service.inter;

import org.springframework.http.ResponseEntity;
import vn.spring.webbansach_backend.Dto.BookDto;

public interface IBookService {
    void addBook(BookDto bookDto);
}
