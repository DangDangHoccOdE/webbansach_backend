package vn.spring.webbansach_backend.service.inter;

import org.springframework.http.ResponseEntity;
import vn.spring.webbansach_backend.dto.EmailDto;
import vn.spring.webbansach_backend.dto.UserDto;

public interface IUserService {
    Boolean existsUserByUsernameAndActiveIsTrue(String username);
    ResponseEntity<?> registerUser(UserDto user);
    ResponseEntity<?> activatedAccount(String email, String activationCode);
    ResponseEntity<?> resendActivationCode(String email);
    ResponseEntity<?> changeInformation(UserDto userDto);
    ResponseEntity<?> changeEmail(EmailDto emailDto);
    ResponseEntity<?> confirmChangeEmail(String email,String emailCode,String newEmail);

}
