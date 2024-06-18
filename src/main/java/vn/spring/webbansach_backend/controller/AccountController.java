package vn.spring.webbansach_backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import vn.spring.webbansach_backend.entity.Notice;
import vn.spring.webbansach_backend.entity.User;
import vn.spring.webbansach_backend.security.JwtResponse;
import vn.spring.webbansach_backend.security.LoginRequest;
import vn.spring.webbansach_backend.service.impl.JwtService;
import vn.spring.webbansach_backend.service.inter.IAccountService;
import vn.spring.webbansach_backend.service.inter.IUserService;

@RestController
@RequestMapping("/account")
@CrossOrigin(origins = "*")
public class AccountController {
    @Autowired
    private IAccountService iAccountService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private IUserService iUserService;
    @Autowired
    private JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Validated @RequestBody User user){
        ResponseEntity<?> respone = iAccountService.registerUser(user);
        return respone;
    }

    @GetMapping("/activatedAccount")
    public ResponseEntity<?> activatedAccount(@RequestParam String email,@RequestParam String activationCode){
            ResponseEntity<?> response = iAccountService.activatedAccount(email, activationCode);
            return response;
    }
    @GetMapping("/resendActivationCode")
    public ResponseEntity<?> resendActivationCode(@RequestParam String email){
        ResponseEntity<?> response = iAccountService.resendActivationCode(email);
        return response;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Validated @RequestBody LoginRequest loginRequest){
        try{
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),loginRequest.getPassword())
            );

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
}
