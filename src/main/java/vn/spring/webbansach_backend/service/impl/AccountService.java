package vn.spring.webbansach_backend.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import vn.spring.webbansach_backend.dao.UserRepository;
import vn.spring.webbansach_backend.entity.Notice;
import vn.spring.webbansach_backend.entity.User;
import vn.spring.webbansach_backend.service.inter.IAccountService;
import vn.spring.webbansach_backend.service.inter.IEmailService;

import java.util.UUID;

@Service
public class AccountService implements IAccountService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private IEmailService iEmailService;

    public ResponseEntity<?> registerUser(User user){
        // check username and email valid
        if(userRepository.existsByUserName(user.getUserName())){
            return ResponseEntity.badRequest().body(new Notice(("Tên tài khoản đã tồn tại!")));
        }
        if(userRepository.existsByEmail(user.getEmail())){
            return ResponseEntity.badRequest().body(new Notice(("Email đã tồn tại!")));
        }

        String encryptPassword = bCryptPasswordEncoder.encode(user.getPassword());
        user.setPassword(encryptPassword);

        // set activation info
        user.setActivationCode(createActivationCode());
        user.setActive(false);

        // save user to DB
        User registedUser = userRepository.save(user);

        // send Email
        sendEmailActive(user.getEmail(),user.getActivationCode());

        return ResponseEntity.ok("Đăng ký thành công!");
    }

    private String createActivationCode(){
        return UUID.randomUUID().toString();
    }

    private void sendEmailActive(String email, String activationCode){
        String subject ="Kích hoạt tài khoản của bạn tại WebBanSach";
        String text = "Vui lòng sử dụng mã sau để kích hoạt tài khoản <"+email+">:<html><body><br>/<h1>"+activationCode+"</h1></body></html>";

        iEmailService.sendMessage("danghoangtest1@gmail.com",email,subject,text);
    }
}
