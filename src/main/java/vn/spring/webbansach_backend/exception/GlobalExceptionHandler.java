package vn.spring.webbansach_backend.exception;

import org.springframework.expression.AccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import vn.spring.webbansach_backend.entity.Notice;

import java.nio.file.AccessDeniedException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler({JwtTokenExpiredException.class, HttpClientErrorException.Unauthorized.class})
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    public ResponseEntity<?> handleJwtTokenExpiredException(JwtTokenExpiredException e) {
        System.out.println("Error: "+e.getMessage());
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> handleAccessDeniedException(AccessDeniedException e){
        System.out.println("AccessDenied: "+e.getMessage());
        return ResponseEntity.badRequest().body(new Notice("Bạn không có quyền truy cập!"));
    }
}
