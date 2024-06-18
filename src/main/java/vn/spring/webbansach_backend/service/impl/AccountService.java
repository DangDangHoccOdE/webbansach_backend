package vn.spring.webbansach_backend.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import vn.spring.webbansach_backend.dao.UserRepository;
import vn.spring.webbansach_backend.entity.Notice;
import vn.spring.webbansach_backend.entity.User;
import vn.spring.webbansach_backend.service.inter.IAccountService;
import vn.spring.webbansach_backend.service.inter.IEmailService;

import java.time.LocalDateTime;
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
        LocalDateTime expiryTime = LocalDateTime.now().plusMinutes(3);
        user.setActivationExpiry(expiryTime);

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
        String text = "Vui lòng sử dụng mã sau để kích hoạt tài khoản :"+email+":<html><body><br/><h1>"+activationCode+"</h1></body></html>";
        text+="<br/> Click vào đường link để kích hoạt tài khoản (Thời gian hết hạn :<b>3 phút<b/>)";
        String url="http://localhost:3000/activatedAccount/"+email+"/"+activationCode;
        text+="<br/> <a href="+url+">"+url+"</a> ";

        iEmailService.sendMessage("danghoangtest1@gmail.com",email,subject,text);
    }

    public ResponseEntity<?> activatedAccount(String email, String activationCode){
        User user = userRepository.findByEmail(email);
        if(user==null){
            return ResponseEntity.badRequest().body(new Notice("Người dùng không tồn tại!"));
        }
         if(user.getActivationExpiry().isBefore(LocalDateTime.now())){
            return ResponseEntity.badRequest().body(new Notice("Mã kích hoạt đã hết hạn!"));
        }
         if(user.isActive()){
            return ResponseEntity.badRequest().body(new Notice("Tài khoản dùng đã được kích hoạt!"));
        }
         if(activationCode.equals(user.getActivationCode())){
            user.setActive(true);
            userRepository.save(user);
            return ResponseEntity.ok(new Notice("Đã kích hoạt tài khoản thành công, đăng nhập tài khoản và sử dụng dịch vụ!"));
        }else{
            return ResponseEntity.badRequest().body(new Notice("Mã kích hoạt không chính xác!"));
        }

    }

    int counter = 0;
    public ResponseEntity<?> resendActivationCode(String email){
        User user = userRepository.findByEmail(email);
        if(user == null){
            return ResponseEntity.badRequest().body(new Notice("Tài khoản email không tồn tại!"));
        }
        if(user.isActive()){
            return ResponseEntity.badRequest().body(new Notice("Tài khoản dùng đã được kích hoạt!"));
        }

        String activationCodeNew = createActivationCode();
        user.setActivationCode(activationCodeNew);
        LocalDateTime expiry = LocalDateTime.now().plusMinutes(5);
        user.setActivationExpiry(expiry);

        userRepository.save(user);

        // Resend email
        sendEmailActive(email,activationCodeNew);

        return ResponseEntity.ok().body(new Notice("Mã kích hoạt mới đã được gửi tới email của bạn."));
    }
}
