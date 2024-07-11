package vn.spring.webbansach_backend.controller;

import com.fasterxml.jackson.databind.util.JSONPObject;
import jakarta.servlet.http.HttpServletRequest;
import netscape.javascript.JSObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import vn.spring.webbansach_backend.dto.EmailDto;
import vn.spring.webbansach_backend.dto.PasswordDto;
import vn.spring.webbansach_backend.dto.UserDto;
import vn.spring.webbansach_backend.entity.Notice;
import vn.spring.webbansach_backend.entity.User;
import vn.spring.webbansach_backend.security.JwtResponse;
import vn.spring.webbansach_backend.security.LoginRequest;
import vn.spring.webbansach_backend.service.impl.JwtService;
import vn.spring.webbansach_backend.service.inter.IUserService;

import java.util.Map;
import net.minidev.json.JSONObject;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private IUserService iUserService;
    @Autowired
    private JwtService jwtService;
    @GetMapping("/findUserByUsername")
    public ResponseEntity<JSONObject> findUserByUsername(@RequestParam String username){
        User user = iUserService.findUserByUsername(username);
        JSONObject data = new JSONObject();
        if(user ==null){
            data.put("notice","Không tìm thấy người sử dụng!");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(data);
        }
        data.put("userId",user.getUserId());
        data.put("firstName",user.getFirstName());
        data.put("lastName",user.getLastName());
        data.put("userName",user.getUserName());
        data.put("dateOfBirth",user.getDateOfBirth());
        data.put("phoneNumber",user.getPhoneNumber());
        data.put("password",user.getPassword());
        data.put("sex",user.getSex());
        data.put("email",user.getEmail());
        data.put("deliveryAddress",user.getDeliveryAddress());
        data.put("purchaseAddress",user.getPurchaseAddress());
        data.put("avatar",user.getAvatar());
        data.put("active",user.isActive());
        return ResponseEntity.ok(data);
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Validated @RequestBody UserDto user) {
        ResponseEntity<?> response = iUserService.registerUser(user);
        return response;
    }

    @GetMapping("/activatedAccount")
    public ResponseEntity<?> activatedAccount(@RequestParam String email, @RequestParam String activationCode) {
        ResponseEntity<?> response = iUserService.activatedAccount(email, activationCode);
        return response;
    }

    @GetMapping("/resendActivationCode")
    public ResponseEntity<?> resendActivationCode(@RequestParam String email) {
        ResponseEntity<?> response = iUserService.resendActivationCode(email);
        return response;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Validated @RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
            );

            if (!iUserService.existsUserByUsernameAndActiveIsTrue(loginRequest.getUsername())) {
                return ResponseEntity.badRequest().body(new Notice("Tài khoản của bạn chưa được kích hoạt, vui lòng vào email để kích hoat!"));
            }

            if (authentication.isAuthenticated()) {
                final String jwt = jwtService.generateToken(loginRequest.getUsername());
                final String refreshToken = jwtService.generateRefreshToken(loginRequest.getUsername());
                return ResponseEntity.ok(new JwtResponse(jwt, refreshToken));
            }
        } catch (AuthenticationException e) {
            return ResponseEntity.badRequest().body(new Notice("Tên đăng nhập hoặc mặt khẩu không chính xác!"));
        }
        return ResponseEntity.badRequest().body(new Notice("Xác thực không thành công!"));
    }


    @PostMapping("/refreshToken")
    public ResponseEntity<?> refreshToken(@RequestHeader(name = "X-Refresh-Token") String refreshToken) {
        if (refreshToken != null && refreshToken.startsWith("Refresh-Token ")) {
            refreshToken = refreshToken.substring(14);
        }
        if (refreshToken != null && jwtService.validateRefreshToken(refreshToken, JwtService.SECRET_REFRESH_TOKEN)) {
            String username = jwtService.extractUserName(refreshToken, JwtService.SECRET_REFRESH_TOKEN);
            if (username != null) {
                String newAccessToken = jwtService.generateToken(username);
                return ResponseEntity.ok(new JwtResponse(newAccessToken, refreshToken));
            }
        }
        return ResponseEntity.badRequest().body(new Notice("Refresh token không hợp lệ!"));
    }


    @PutMapping("/changeInfo")
    public ResponseEntity<?> changeInfo(@Validated @RequestBody UserDto userDto) {
        return iUserService.changeInformation(userDto);
    }

    @PutMapping("/changeEmail")
    public ResponseEntity<?> changeEmail(@Validated @RequestBody EmailDto emailDto) {
        return iUserService.changeEmail(emailDto);
    }

    @GetMapping("/confirmChangeEmail")
    public ResponseEntity<?> confirmChangeEmail(@RequestParam String email,@RequestParam String emailCode,@RequestParam String newEmail){
        return iUserService.confirmChangeEmail(email,emailCode,newEmail);
    }

    @PostMapping("/forgotPassword")
    public ResponseEntity<?> forgotPassword(@RequestBody Map<String,String> map){
        String username = map.get("username");
        return iUserService.forgotPassword(username);
    }

    @GetMapping("/confirmForgotPassword")
    public ResponseEntity<?> confirmForgotPassword(@RequestParam String username,@RequestParam String forgotPasswordCode){
        return iUserService.confirmForgotPassword(username,forgotPasswordCode);
    }

    @PostMapping("/passwordChange")
    public ResponseEntity<?> passwordChange(@RequestBody PasswordDto passwordDto){
        String username = passwordDto.getUsername();
        String password = passwordDto.getPassword();
        String duplicatePassword = passwordDto.getDuplicatePassword();
        return iUserService.passwordChange(username,password,duplicatePassword);
    }

    @DeleteMapping("/deleteUser/{username}")
    public ResponseEntity<?> deleteUser(@PathVariable String username){
        return iUserService.deleteUser(username);
    }

}
