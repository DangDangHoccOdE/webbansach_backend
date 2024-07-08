package vn.spring.webbansach_backend.aop;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;
import vn.spring.webbansach_backend.dto.UserDto;
import vn.spring.webbansach_backend.utils.SecurityUtils;

@Aspect
@Component
public class UserAOP {
    @Autowired
    private SecurityUtils securityUtils;
    @Before("execution(* vn.spring.webbansach_backend.controller.UserController.findUserByUsername(..)) && args(..,username)")
    public void hasAccess(String username){
        if(!securityUtils.hasAccessByUsername(username)){
            System.out.println("Không có quyền truy cập!");
            throw new AccessDeniedException("Bạn không có quyền truy cập!");
        }
    }

}
