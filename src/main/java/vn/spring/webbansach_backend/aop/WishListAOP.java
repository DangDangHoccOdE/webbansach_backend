package vn.spring.webbansach_backend.aop;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;
import vn.spring.webbansach_backend.utils.SecurityUtils;

@Aspect
@Component
public class WishListAOP {
    @Autowired
    private SecurityUtils securityUtils;

    @Before("execution(* vn.spring.webbansach_backend.controller.WishListController.showWishList(..)) && args(..,userId)")
    public void hasAccess(Long userId) throws AccessDeniedException{
        if (!securityUtils.hasAccessByUserId(userId)) {
            System.out.println("Không có quyền truy cập!");
            throw new AccessDeniedException("Bạn không có quyền truy cập!");
        }
    }

}
