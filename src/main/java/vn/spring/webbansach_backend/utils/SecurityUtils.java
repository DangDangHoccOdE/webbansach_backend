package vn.spring.webbansach_backend.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import vn.spring.webbansach_backend.entity.User;
import vn.spring.webbansach_backend.service.IUserSecurityService;

@Component
public class SecurityUtils {
    private final IUserSecurityService iUserSecurityService;

    @Autowired
    public SecurityUtils(IUserSecurityService iUserSecurityService) {
        this.iUserSecurityService = iUserSecurityService;
    }

    public boolean hasAccessByUserId(Long userId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return isAdmin(authentication) ||  isUserIdMatch(authentication,userId);
    }

    public boolean hasAccessByUsername(String username) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return isAdmin(authentication) ||  isUserUsernameMatch(authentication,username);
    }
    private boolean isAdmin(Authentication authentication){
        return authentication.getAuthorities().stream()
                .anyMatch(authority->authority.getAuthority().equals("ROLE_ADMIN"));
    }

    private boolean isUserIdMatch(Authentication authentication,Long userId){
        Long authenticationUserId = getUserIdFromPrincipal(authentication);
        return authenticationUserId!=null && authenticationUserId.equals(userId);
    }

    private boolean isUserUsernameMatch(Authentication authentication,String username){
        Long authenticationUserId = getUserIdFromPrincipal(authentication);
        return authenticationUserId!=null && getUsernameFromPrincipal(authentication).equals(username);
    }

    private Long getUserIdFromPrincipal(Authentication authentication){
        User user = iUserSecurityService.findByUserName(authentication.getName());
        return user.getUserId();
    }
    private String getUsernameFromPrincipal(Authentication authentication){
        User user = iUserSecurityService.findByUserName(authentication.getName());
        return user.getUserName();
    }
}
