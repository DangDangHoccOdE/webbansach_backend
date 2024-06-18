package vn.spring.webbansach_backend.service.inter;

import org.springframework.http.ResponseEntity;
import vn.spring.webbansach_backend.entity.User;

public interface IAccountService{
     ResponseEntity<?> registerUser(User user);
     ResponseEntity<?> activatedAccount(String email, String activationCode);
     ResponseEntity<?> resendActivationCode(String email);

}