package vn.spring.webbansach_backend.service.inter;

import org.springframework.http.ResponseEntity;
import vn.spring.webbansach_backend.dto.EmailDto;
import vn.spring.webbansach_backend.dto.UserDto;
import vn.spring.webbansach_backend.entity.User;
import vn.spring.webbansach_backend.entity.WishList;

import java.nio.file.AccessDeniedException;
import java.security.Principal;
import java.util.*;

public interface IUserService {
    User saveUser(User user);
    List<User> findAllUsers();
    Boolean existsUserByUsernameAndActiveIsTrue(String username);
    User findUserByUserId(Long id);
    User findUserByUsername(String username);

    ResponseEntity<?> registerUser(UserDto user);
    ResponseEntity<?> activatedAccount(String email, String activationCode);
    ResponseEntity<?> resendActivationCode(String email);
    ResponseEntity<?> changeInformation(UserDto userDto);
    ResponseEntity<?> changeEmail(EmailDto emailDto);
    ResponseEntity<?> confirmChangeEmail(String email,String emailCode,String newEmail);
    ResponseEntity<?> forgotPassword(String username);
    ResponseEntity<?> confirmForgotPassword(String username,String forgotPasswordCode);
    ResponseEntity<?> passwordChange(String username,String password,String duplicatePassword);
    ResponseEntity<?> deleteUser(String username);

}
