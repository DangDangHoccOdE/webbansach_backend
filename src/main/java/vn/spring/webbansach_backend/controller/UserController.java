package vn.spring.webbansach_backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    private final AuthenticationManager authenticationManager;
    private final IUserService iUserService;
    private final JwtService jwtService;
    @Autowired
    public UserController(AuthenticationManager authenticationManager, IUserService iUserService, JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.iUserService = iUserService;
        this.jwtService = jwtService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserByUserId(@PathVariable Long userId){
        User user = iUserService.findUserByUserId(userId);

        return putUserData(user);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/findUserByCondition")
    public ResponseEntity<JSONObject> findUserByCondition(@RequestParam String condition){
        User user = iUserService.findUserByUsername(condition); // Tìm kiếm theo họ tên

        if(user == null) { // Không tồn tại user theo username
            user = iUserService.findUserByEmail(condition);
        }
        // Nếu không tồn tai user theo email
        return putUserData(user);
    }

    private ResponseEntity<JSONObject> putUserData(User user){
        if(user == null){ // Nếu không tồn tai user theo email
            JSONObject data = new JSONObject();
            data.put("notice", "Không tìm thấy người sử dụng!");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(data);
        }

        JSONObject data = new JSONObject(); // nêu tồn tại user

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
        return iUserService.registerUser(user);
    }

    @GetMapping("/activatedAccount")
    public ResponseEntity<?> activatedAccount(@RequestParam String email, @RequestParam String activationCode) {
        return iUserService.activatedAccount(email, activationCode);
    }

    @GetMapping("/resendActivationCode")
    public ResponseEntity<?> resendActivationCode(@RequestParam String email) {
        return iUserService.resendActivationCode(email);
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

                User user = iUserService.findUserByUsername(loginRequest.getUsername()); // Lưu refreshToken vào db
                user.setRefreshToken(refreshToken);
                iUserService.saveUser(user);
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
                User user = iUserService.findUserByUsername(username);
                if(user.getRefreshToken().equals(refreshToken)){
                    String newAccessToken = jwtService.generateToken(username);
                    return ResponseEntity.ok(new JwtResponse(newAccessToken, refreshToken));
                }
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

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/deleteUser/{username}")
    public ResponseEntity<?> deleteUser(@PathVariable String username){
        return iUserService.deleteUser(username);
    }


}
