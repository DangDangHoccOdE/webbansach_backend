package vn.spring.webbansach_backend.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import vn.spring.webbansach_backend.Dto.UserDto;
import vn.spring.webbansach_backend.dao.RoleRepository;
import vn.spring.webbansach_backend.dao.UserRepository;
import vn.spring.webbansach_backend.entity.Notice;
import vn.spring.webbansach_backend.entity.Role;
import vn.spring.webbansach_backend.entity.User;
import vn.spring.webbansach_backend.service.inter.IEmailService;
import vn.spring.webbansach_backend.service.inter.IUserService;
import vn.spring.webbansach_backend.utils.ConvertStringToDate;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService implements IUserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private IEmailService iEmailService;

    @Override
    public Boolean existsUserByUsernameAndActiveIsTrue(String username) {
        return userRepository.existsByUserNameAndActiveIsTrue(username);
    }
    @Override
    public ResponseEntity<?> registerUser(UserDto userDto){
        User user = new User();
        // check username and email valid
        if(userRepository.existsByUserName(userDto.getUserName())){
            return ResponseEntity.badRequest().body(new Notice(("Tên tài khoản đã tồn tại!")));
        }
        if(userRepository.existsByEmail(userDto.getEmail())){
            return ResponseEntity.badRequest().body(new Notice(("Email đã tồn tại!")));
        }

        // handle Image size
        if(userDto.getAvatar() != null && !userDto.getAvatar().isEmpty()){
            byte[] compressedImage =  userDto.getAvatar().getBytes();
            if(compressedImage.length>1048576){
                return ResponseEntity.badRequest().body(new Notice(("Đã gặp lỗi do dung luượng ảnh quá lớn hoặc bị lỗi, vui lòng chọn ảnh khác!!")));
            }
        }

        String encryptPassword = bCryptPasswordEncoder.encode(userDto.getPassword());

        // Get info userDto -> user
        user.setUserName(userDto.getUserName());
        user.setEmail(userDto.getEmail());
        user.setPassword(encryptPassword);
        user.setAvatar((userDto.getAvatar()));
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setLastName(userDto.getLastName());
        user.setDateOfBirth(ConvertStringToDate.convert(userDto.getDateOfBirth()));
        user.setPhoneNumber(userDto.getPhoneNumber());
        user.setSex(userDto.getSex());

        // set activation info
        user.setActivationCode(createActivationCode());
        user.setActive(false);
        LocalDateTime expiryTime = LocalDateTime.now().plusMinutes(3);
        user.setActivationExpiry(expiryTime);

        // save user to DB
        User registedUser = userRepository.save(user);

        // send Email
        sendEmailActive(userDto.getEmail(),userDto.getActivationCode());

        return ResponseEntity.ok(new Notice("Đăng ký thành công, vui lòng kiểm tra email để kích hoạt!"));
    }

    private String createActivationCode(){
        return UUID.randomUUID().toString();
    }

    @Override
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

    @Override
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

    private void sendEmailActive(String email, String activationCode) {
        String subject = "Kích hoạt tài khoản của bạn tại WebBanSach";
        String text = "Vui lòng sử dụng mã sau để kích hoạt tài khoản :" + email + ":<html><body><br/><h1>" + activationCode + "</h1></body></html>";
        text += "<br/> Click vào đường link để kích hoạt tài khoản (Thời gian hết hạn :<b>3 phút<b/>)";
        String url = "http://localhost:3000/activatedAccount/" + email + "/" + activationCode;
        text += "<br/> <a href=" + url + ">" + url + "</a> ";

        iEmailService.sendMessage("danghoangtest1@gmail.com", email, subject, text);
    }

    @Override
    public ResponseEntity<?> changeInformation(UserDto userDto) {
        User user = userRepository.findByUserName(userDto.getUserName());

        if(userRepository.existsByEmail(userDto.getEmail()) && !userDto.getEmail().equals(user.getEmail())){
            return ResponseEntity.badRequest().body(new Notice("Email đã tồn tại !"));
        }
        user.setEmail(userDto.getEmail());
        user.setSex(userDto.getSex());
        user.setPhoneNumber(userDto.getPhoneNumber());
        user.setLastName(userDto.getLastName());
        user.setFirstName(userDto.getFirstName());
        user.setDateOfBirth(ConvertStringToDate.convert(userDto.getDateOfBirth()));
        user.setAvatar(userDto.getAvatar());
        user.setPurchaseAddress(userDto.getPurchaseAddress());
        user.setDeliveryAddress(userDto.getDeliveryAddress());

        try{
            userRepository.save(user);
            return ResponseEntity.ok(new Notice("Đã thay đổi thông tin thành công!"));
        }catch (Exception e){
            System.out.println(e.getMessage());
            return ResponseEntity.badRequest().body(new Notice("Đã gặp lỗi, không thể chỉnh sửa thông tin thành công!"));
        }

    }
}
