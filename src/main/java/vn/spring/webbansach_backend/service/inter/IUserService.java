package vn.spring.webbansach_backend.service.inter;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import vn.spring.webbansach_backend.Dto.UserDto;
import vn.spring.webbansach_backend.entity.User;

public interface IUserService {
    Boolean existsUserByUsernameAndActiveIsTrue(String username);
    ResponseEntity<?> registerUser(UserDto user);
    ResponseEntity<?> activatedAccount(String email, String activationCode);
    ResponseEntity<?> resendActivationCode(String email);
    ResponseEntity<?> changeInformation(UserDto userDto);

}
