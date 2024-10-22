package vn.spring.webbansach_backend.exception;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import vn.spring.webbansach_backend.entity.Notice;


@RestControllerAdvice
public class GlobalExceptionHandler{
    @ExceptionHandler(UsernameNotFoundException.class)  //Username not found
    public ResponseEntity<?> handleUsernameNotFoundException(UsernameNotFoundException e) {
        System.out.println("UsernameNotFound: "+e.getMessage());
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class) // Không có quyền truy cập
    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    public ResponseEntity<?> handleAccessDeniedException(AccessDeniedException e) {
        System.out.println(e.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new Notice(e.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class) // 1 trường trong dto không thể bị empty
    public ResponseEntity<Notice> handleNotEmpty(MethodArgumentNotValidException e) {
        String error = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Notice(error));
    }
    @ExceptionHandler(BadCredentialsException.class) // mật khẩu sai
    public ResponseEntity<Notice> handleBadCredentials(BadCredentialsException e) {
        System.out.println("BadCredentialsException:"+e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new Notice("Mật khẩu không đúng"));
    }

    @ExceptionHandler(InternalAuthenticationServiceException.class)
    public ResponseEntity<Notice> handleInternalAuthenticationServiceException(InternalAuthenticationServiceException e) {
        System.out.println("InternalAuthenticationServiceException:"+e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new Notice("Tài khoản không đúng"));
    }

    @ExceptionHandler(ExpiredJwtException.class) // Token hết hạn
    public ResponseEntity<Notice> handleExpiredJwtException(ExpiredJwtException e){
        System.out.println("ExpiredJwtException: "+e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new Notice("Token đã hết hạn"));
    }

    @ExceptionHandler(UnsupportedJwtException.class) // Token không hỗ trợ
    public ResponseEntity<?> handleUnsupportedJwtException(UnsupportedJwtException e){
        System.out.println("UnsupportedJwtException: "+e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new Notice("Token không được hỗ trợ"));
    }
    @ExceptionHandler(MalformedJwtException.class) // token không đúng định dạng
    public ResponseEntity<?> handleMalformedJwtException(MalformedJwtException e){
        System.out.println("MalformedJwtException: "+e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new Notice("Token không đúng định dạng"));
    }
    @ExceptionHandler(SignatureException.class) // Chữ ký token không đúng
    public ResponseEntity<?> handleSignatureException(SignatureException e){
        System.out.println("SignatureException: "+e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new Notice("Chữ ký token không hợp lệ"));
    }

    @ExceptionHandler(IllegalArgumentException.class) // Token không hợp lệ
    public ResponseEntity<?> handleIllegalArgumentException(IllegalArgumentException e){
        System.out.println("IllegalArgumentException: "+e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new Notice("Token không hợp lệ"));
    }



}
