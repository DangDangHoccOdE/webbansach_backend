package vn.spring.webbansach_backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import vn.spring.webbansach_backend.entity.Notice;


@RestControllerAdvice
public class GlobalExceptionHandler{
    @ExceptionHandler({JwtTokenExpiredException.class, HttpClientErrorException.Unauthorized.class})
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    public ResponseEntity<?> handleJwtTokenExpiredException(JwtTokenExpiredException e) {
        System.out.println("Error: "+e.getMessage());
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler({AccessDeniedException.class,})
    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    public ResponseEntity<?> handleAccessDeniedException(Exception e) {
        System.out.println("AccessDenied: " + e.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new Notice(e.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Notice> handleException(Exception e) {
        System.out.println("Exception: " + e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Notice("Lỗi không xác định!"));
    }
}
