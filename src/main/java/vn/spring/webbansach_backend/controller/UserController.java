package vn.spring.webbansach_backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import vn.spring.webbansach_backend.Dto.UserDto;
import vn.spring.webbansach_backend.entity.Notice;
import vn.spring.webbansach_backend.security.JwtResponse;
import vn.spring.webbansach_backend.security.LoginRequest;
import vn.spring.webbansach_backend.service.impl.JwtService;
import vn.spring.webbansach_backend.service.inter.IUserService;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private IUserService iUserService;
    @Autowired
    private JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Validated @RequestBody UserDto user){
        ResponseEntity<?> respone = iUserService.registerUser(user);
        return respone;
    }

    @GetMapping("/activatedAccount")
    public ResponseEntity<?> activatedAccount(@RequestParam String email,@RequestParam String activationCode){
            ResponseEntity<?> response = iUserService.activatedAccount(email, activationCode);
            return response;
    }
    @GetMapping("/resendActivationCode")
    public ResponseEntity<?> resendActivationCode(@RequestParam String email){
        ResponseEntity<?> response = iUserService.resendActivationCode(email);
        return response;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Validated @RequestBody LoginRequest loginRequest){
        try{
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),loginRequest.getPassword())
            );

            if(!iUserService.existsUserByUsernameAndActiveIsTrue(loginRequest.getUsername())){
                return ResponseEntity.badRequest().body(new Notice("Tài khoản của bạn chưa được kích hoạt, vui lòng vào email để kích hoat!"));
            }
            // authentication success
            if(authentication.isAuthenticated()){
                final String jwt = jwtService.generateToken(loginRequest.getUsername());
                return ResponseEntity.ok(new JwtResponse(jwt));
            }
        }catch(AuthenticationException e){
                return ResponseEntity.badRequest().body(new Notice("Tên đăng nhập hoặc mặt khẩu không chính xác!"));
        }
        return ResponseEntity.badRequest().body(new Notice("Xác thực không thành công!"));
    }

    @PutMapping("/changeInfo")
    public ResponseEntity<?> changeInfo(@Validated @RequestBody UserDto userDto){
        System.out.println(userDto);
        return iUserService.changeInformation(userDto);
    }
}
