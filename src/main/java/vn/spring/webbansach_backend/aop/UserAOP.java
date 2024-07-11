package vn.spring.webbansach_backend.aop;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;
import vn.spring.webbansach_backend.utils.SecurityUtils;

@Aspect
@Component
public class UserAOP {
    @Autowired
    private SecurityUtils securityUtils;
    @Before("(execution(* vn.spring.webbansach_backend.controller.UserController.findUserByUsername(..))" +
            "|| execution(* vn.spring.webbansach_backend.controller.UserController.deleteUser(..))) && args(..,username)")
    public void hasAccess(String username) throws AccessDeniedException{
        if(!securityUtils.hasAccessByUsername(username)){
            throw new AccessDeniedException("Bạn không có quyền truy cập!");
        }
    }

}
