package vn.spring.webbansach_backend.service.inter;

import org.springframework.security.core.userdetails.UserDetailsService;
import vn.spring.webbansach_backend.entity.User;

public interface IUserService extends UserDetailsService {
    User findByUserName(String username);
}
