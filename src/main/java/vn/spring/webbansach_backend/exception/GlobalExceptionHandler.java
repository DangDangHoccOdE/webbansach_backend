package vn.spring.webbansach_backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler({JwtTokenExpiredException.class, HttpClientErrorException.Unauthorized.class})
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    public ResponseEntity<?> handleJwtTokenExpiredException(JwtTokenExpiredException e) {
        System.out.println("Error: "+e.getMessage());
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
