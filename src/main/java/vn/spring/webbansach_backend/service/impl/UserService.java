package vn.spring.webbansach_backend.service.impl;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import vn.spring.webbansach_backend.dto.EmailDto;
import vn.spring.webbansach_backend.dto.UserDto;
import vn.spring.webbansach_backend.dao.UserRepository;
import vn.spring.webbansach_backend.dto.UserOauth2Dto;
import vn.spring.webbansach_backend.entity.*;
import vn.spring.webbansach_backend.service.inter.IEmailService;
import vn.spring.webbansach_backend.service.inter.IRoleService;
import vn.spring.webbansach_backend.service.inter.IUserService;
import vn.spring.webbansach_backend.utils.ConvertStringToDate;
import vn.spring.webbansach_backend.utils.MaskEmail;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UserService implements IUserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final IEmailService iEmailService;
    private final IRoleService iRoleService;
    private AuthProvider authProvider;
    @Autowired
    public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, IEmailService iEmailService, IRoleService iRoleService) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.iEmailService = iEmailService;
        this.iRoleService = iRoleService;
    }

    @Override
    public User saveUser(User user) {
       return userRepository.save(user);
    }

    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Boolean existsUserByUsernameAndActiveIsTrue(String username) {
        return userRepository.existsByUserNameAndActiveIsTrue(username);
    }

    @Override
    public User findUserByUserId(Long id) {
        return userRepository.findByUserId(id);
    }

    @Override
    public User findUserByUsername(String username) {
        return userRepository.findByUserName(username);
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    @Transactional
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
                return ResponseEntity.badRequest().body(new Notice(("Đã gặp lỗi do dung lượng ảnh quá lớn hoặc bị lỗi, vui lòng chọn ảnh khác!")));
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
        user.setProvider(AuthProvider.local);
        // set activation info
        user.setActivationCode(createRandomCode());
        user.setActive(false);
        LocalDateTime expiryTime = LocalDateTime.now().plusMinutes(10);
        user.setActivationExpiry(expiryTime);

        // save user với role user
        List<String> roles = new ArrayList<>();
        roles.add("ROLE_USER");
        saveUserWithRole(user,roles);

        // send Email
        sendEmailActive(userDto.getEmail(),user.getActivationCode());

        return ResponseEntity.ok(new Notice("Đăng ký thành công, vui lòng kiểm tra email để kích hoạt!"));
    }

    private String createRandomCode(){
        return UUID.randomUUID().toString();
    }

    @Transactional
    @Override
    public User saveUserWithRole(User user, List<String> roleName) { // Tránh lỗi bị detached Role khi lưu bên oauth register
        List<Role> roles = new ArrayList<>();
        for(String role:roleName){
            Role findRole = iRoleService.findRoleByName(role);
            if(findRole == null){
                findRole = new Role();
                findRole.setRoleName(role);
            }
            roles.add(findRole);
        }
        user.setRoleList(roles);
       return userRepository.save(user);
    }

    @Override
    public ResponseEntity<?> activatedAccount(String email, String activationCode){
        User user = userRepository.findByEmail(email);
        if(user==null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Notice("Người dùng không tồn tại!"));
        }
        if(user.getActivationExpiry().isBefore(LocalDateTime.now())){
            return ResponseEntity.badRequest().body(new Notice("Mã kích hoạt đã hết hạn!"));
        }
        if(user.isActive()){
            return ResponseEntity.badRequest().body(new Notice("Tài khoản dùng đã được kích hoạt!"));
        }
        if(activationCode.equals(user.getActivationCode())){
            user.setActive(true);

            // Tạo giỏ hàng cho user
            CartItem cartItem = new CartItem();
            cartItem.setUser(user);

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
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Notice("Tài khoản email không tồn tại!"));
        }
        if(user.isActive()){
            return ResponseEntity.badRequest().body(new Notice("Tài khoản dùng đã được kích hoạt!"));
        }

        String activationCodeNew = createRandomCode();
        user.setActivationCode(activationCodeNew);
        LocalDateTime expiry = LocalDateTime.now().plusMinutes(10);
        user.setActivationExpiry(expiry);

        userRepository.save(user);

        // Resend email
        sendEmailActive(email,activationCodeNew);

        return ResponseEntity.ok().body(new Notice("Mã kích hoạt mới đã được gửi tới email của bạn."));
    }

    private void sendEmailActive(String email, String activationCode) {
        String subject = "Kích hoạt tài khoản của bạn tại WebBanSach";
        String text = "Vui lòng sử dụng mã sau để kích hoạt tài khoản :" + email + ":<html><body><br/><h1>" + activationCode + "</h1></body></html>";
        text += "<br/> Click vào đường link để kích hoạt tài khoản (Thời gian hết hạn :<b>10 phút<b/>)";
        String url = "http://localhost:3000/activatedAccount/" + email + "/" + activationCode;
        text += "<br/> <a href=" + url + ">" + url + "</a> ";

        iEmailService.sendMessage("danghoangtest1@gmail.com", email, subject, text);
    }

    @Override
    public ResponseEntity<?> changeInformation(UserDto userDto) {
        User user = userRepository.findByUserName(userDto.getUserName());
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

    @Override
    public ResponseEntity<?> changeInformationOauth2(UserOauth2Dto userOauth2Dto) { // Thay đổi thông tin nếu tài khoản đó provider là google không có username password
        User user = userRepository.findByEmail(userOauth2Dto.getEmail());
        user.setSex(userOauth2Dto.getSex());
        user.setPhoneNumber(userOauth2Dto.getPhoneNumber());
        user.setLastName(userOauth2Dto.getLastName());
        user.setFirstName(userOauth2Dto.getFirstName());
        user.setDateOfBirth(ConvertStringToDate.convert(userOauth2Dto.getDateOfBirth()));
        user.setAvatar(userOauth2Dto.getAvatar());
        user.setPurchaseAddress(userOauth2Dto.getPurchaseAddress());
        user.setDeliveryAddress(userOauth2Dto.getDeliveryAddress());

        try{
            userRepository.save(user);
            return ResponseEntity.ok(new Notice("Đã thay đổi thông tin thành công!"));
        }catch (Exception e){
            System.out.println(e.getMessage());
            return ResponseEntity.badRequest().body(new Notice("Đã gặp lỗi, không thể chỉnh sửa thông tin thành công!"));
        }
    }

    @Override
    public ResponseEntity<?> changeEmail(EmailDto emailDto) {
        User user = userRepository.findByEmail(emailDto.getEmail());
        boolean existsNewEmail = userRepository.existsByEmail(emailDto.getNewEmail());
        if(user==null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Notice("Email không tồn tại!"));
        }

        if(emailDto.getEmail().equals(emailDto.getNewEmail())){
            return ResponseEntity.badRequest().body(new Notice("Email mới không thể trùng email cũ!"));
        }

        if(existsNewEmail){
            return ResponseEntity.badRequest().body(new Notice("Email mới đã tồn tại!"));
        }
        user.setEmailCode(createRandomCode());
        LocalDateTime lcd = LocalDateTime.now().plusMinutes(10);
        user.setEmailExpiry(lcd);
        userRepository.save(user);

        // send change email;
        sendEmailChange(user.getEmail(),user.getEmailCode(),emailDto.getNewEmail());

        return ResponseEntity.ok(new Notice("Thay đổi email thành công, vui lòng vào email để xác nhận!"));
    }

    private void sendEmailChange(String email,String emailCode,String emailNew){
        String subject="Thay đổi email của bạn tại WebBanSach";
        String text="Click vào đường link để xác nhận thay đổi email tài khoản (Thời gian hết hạn: <b>10 phút<b/>)";
        String url = "http://localhost:3000/user/confirmChangeEmail/"+email+"/"+emailCode+"/"+emailNew;
        text+= "<br/> <a href="+url+">"+url+"</a>";
        text+="<br/> Email mới của bạn sau khi thay đổi sẽ là: <b>"+emailNew+"<b/>";
        iEmailService.sendMessage("danghoangtest1@gmail.com",email,subject,text);
    }
    @Override
    public ResponseEntity<?> confirmChangeEmail(String email, String emailCode,String newEmail) {
        User user = userRepository.findByEmail(email);
        if(user==null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Notice("Địa chỉ email của bạn đã được thay đổi!"));
        }
        if(user.getEmailExpiry().isBefore(LocalDateTime.now())){
            return ResponseEntity.badRequest().body(new Notice("Mã xác nhận đã hết thời hạn!"));
        }
        if(emailCode.equals(user.getEmailCode())){
            user.setEmail(newEmail);
            userRepository.save(user);
            return ResponseEntity.ok(new Notice("Đã thay đổi email thành công!"));
        }else{
            return ResponseEntity.badRequest().body(new Notice("Mã xác nhận không chính xác!"));
        }
    }

    @Override
    public ResponseEntity<?> forgotPassword(String username) {
        User user = userRepository.findByUserName(username);
        if(user==null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Notice("Không tìm thấy tài khoản!"));
        }

        LocalDateTime lcd = LocalDateTime.now().plusMinutes(10);
        user.setForgotPasswordExpiry(lcd);
        user.setForgotPasswordCode(createRandomCode());
        userRepository.save(user);

        // send email forgot password
        sendEmailForgotPassword(user.getEmail(),username,user.getForgotPasswordCode());
        return ResponseEntity.ok(new Notice("Chúng tôi sẽ gửi link xác nhận đến email "+ MaskEmail.maskEmail(user.getEmail())));
    }

    private void sendEmailForgotPassword(String email,String username,String forgotPasswordCode){
        String subject = "Yêu cầu quên mật khẩu của bạn tại WebBanSach";
        String text = "Click vào đường link xác nhận để thay đổi mật khẩu của tài khoản:<b> "+username+"<b> (Thời gian hết hạn: <b>10 phút<b/>)";
        String url = "http://localhost:3000/user/confirmForgotPassword/"+username+"/"+forgotPasswordCode;
        text+="<br/> <a href="+url+">"+url+"</a>";

        iEmailService.sendMessage("danghoangtest1@gmail.com",email,subject,text);
    }

    @Override
    public ResponseEntity<?> confirmForgotPassword(String username, String forgotPasswordCode) {
        User user = userRepository.findByUserName(username);
        if(user==null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Notice("Người dùng không tồn tại!"));
        }
        if(user.getForgotPasswordExpiry().isBefore(LocalDateTime.now())){
            return ResponseEntity.badRequest().body(new Notice("Mã xác nhận đã hết thời hạn!"));
        }
        if(forgotPasswordCode.equals(user.getForgotPasswordCode())){
            return ResponseEntity.ok(new Notice("Mã xác thực đúng!"));
        }else{
            return ResponseEntity.badRequest().body(new Notice("Mã xác nhận không chính xác!"));
        }
    }

    @Override
    public ResponseEntity<?> passwordChange(String username,String password, String duplicatePassword) {
        User user = userRepository.findByUserName(username);
        if(!password.equals(duplicatePassword)){
            return ResponseEntity.badRequest().body(new Notice("Mật khẩu và mật khẩu nhập lại phải giống nhau!"));
        }

        user.setPassword(bCryptPasswordEncoder.encode(password));
        userRepository.save(user);
        return ResponseEntity.ok(new Notice("Thay đổi mật khẩu thành công!"));
    }

    @Override
    public ResponseEntity<?> deleteUser(String username){
        User user=userRepository.findByUserName(username);
        if(user==null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Notice(("Không tìm thấy user cần xóa!")));
        }

        userRepository.delete(user);
        return ResponseEntity.ok(new Notice("Đã xóa thành công!"));
    }


}
