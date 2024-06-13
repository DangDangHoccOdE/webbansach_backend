package vn.spring.webbansach_backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import vn.spring.webbansach_backend.dao.UserRepository;
import vn.spring.webbansach_backend.entity.Notice;
import vn.spring.webbansach_backend.entity.User;

@Service
public class AccountService {
    @Autowired
    private UserRepository userRepository;

    public ResponseEntity<?> registerUser(User user){
        // check username and email valid
        if(userRepository.existsByUserName(user.getUserName())){
            System.out.println("Lỗi do trùng username");
        }
        if(userRepository.existsByEmail(user.getEmail())){
            System.out.println("Lỗi do trùng email");
        }

        // save user to DB
        User registedUser = userRepository.save(user);
        return ResponseEntity.ok("Đăng ký thành công!");
    }
}
