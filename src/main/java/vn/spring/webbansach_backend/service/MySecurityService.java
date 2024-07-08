package vn.spring.webbansach_backend.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import vn.spring.webbansach_backend.entity.User;

@Service
public class MySecurityService {
    @Autowired
    private IUserSecurityService iUserSecurityService;
    public boolean hasAccess(Authentication authentication, Long userId) {
        return isAdmin(authentication) || isUserIdMatch(authentication, userId);
    }

    private boolean isAdmin(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
    }

    private boolean isUserIdMatch(Authentication authentication, Long userId) {
        Long authenticatedUserId = getUserIdFromPrincipal(authentication);
        return authenticatedUserId != null && authenticatedUserId.equals(userId);
    }

    private Long getUserIdFromPrincipal(Authentication authentication) {
        User user = iUserSecurityService.findByUserName(authentication.getName());
        return user.getUserId();
    }
}
