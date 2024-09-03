package vn.spring.webbansach_backend.aop;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;
import vn.spring.webbansach_backend.dao.UserRepository;
import vn.spring.webbansach_backend.dto.EmailDto;
import vn.spring.webbansach_backend.dto.UserDto;
import vn.spring.webbansach_backend.entity.User;
import vn.spring.webbansach_backend.utils.SecurityUtils;

@Aspect
@Component
public class UserAOP {

    private static final String ACCESS_DENIED_MESSAGE = "Bạn không có quyền truy cập!";

    private final SecurityUtils securityUtils;
    private final UserRepository userRepository;
    @Autowired
    public UserAOP(SecurityUtils securityUtils, UserRepository userRepository) {
        this.securityUtils = securityUtils;
        this.userRepository = userRepository;
    }

    @Before("execution(* vn.spring.webbansach_backend.controller.UserController.getUserByUserId(..)) && args(..,userId)")
    public void hasAccessByUsername(Long userId) throws AccessDeniedException {
        User user = userRepository.findByUserId(userId);
        checkAccessByUser(user);
    }

    @Before(value = "execution(* vn.spring.webbansach_backend.controller.UserController.changeInfo(..)) && args(..,userDto)")
    public void hasAccessByUserDto(UserDto userDto) throws AccessDeniedException {
        checkAccessByUsername(userDto.getUserName());
    }

    @Before(value = "execution(* vn.spring.webbansach_backend.controller.UserController.changeEmail(..)) && args(..,emailDto)")
    public void hasAccessByEmailDto(EmailDto emailDto) throws AccessDeniedException {
        User user = userRepository.findByEmail(emailDto.getEmail());
        checkAccessByUser(user);
    }

    @Before(value = "execution(* vn.spring.webbansach_backend.controller.UserController.confirmChangeEmail(..)) && args(..,email,emailCode,newEmail)", argNames = "email,emailCode,newEmail")
    public void hasAccessByEmail(String email, String emailCode, String newEmail) throws AccessDeniedException {
        User user = userRepository.findByEmail(email);
        checkAccessByUser(user);
    }

    private void checkAccessByUsername(String username) throws AccessDeniedException {
        if (!securityUtils.hasAccessByUsername(username)) {
            throw new AccessDeniedException(ACCESS_DENIED_MESSAGE);
        }
    }

    private void checkAccessByUser(User user) throws AccessDeniedException {
        if (user != null && !securityUtils.hasAccessByUserId(user.getUserId())) {
            throw new AccessDeniedException(ACCESS_DENIED_MESSAGE);
        }
    }
}
