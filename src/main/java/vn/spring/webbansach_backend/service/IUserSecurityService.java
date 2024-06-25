package vn.spring.webbansach_backend.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import vn.spring.webbansach_backend.entity.User;

public interface IUserSecurityService extends UserDetailsService {
    User findByUserName(String username);

}
