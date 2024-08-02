package vn.spring.webbansach_backend.aop;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;
import vn.spring.webbansach_backend.entity.User;
import vn.spring.webbansach_backend.service.inter.IUserService;
import vn.spring.webbansach_backend.utils.SecurityUtils;

@Aspect
@Component
public class OrderAOP {
    private static final String ACCESS_DENIED_MESSAGE = "Bạn không có quyền truy cập!";

    private final IUserService iUserService;
    private final SecurityUtils securityUtils;

    @Autowired
    public OrderAOP(IUserService iUserService, SecurityUtils securityUtils) {
        this.iUserService = iUserService;
        this.securityUtils = securityUtils;
    }

    @Before("execution(* vn.spring.webbansach_backend.controller.OrderController.getBooksOfOrder(..)) && args(..,orderId) ")
    public void checkAccess(Long orderId){

    }

    private void checkAccessByUser(User user) throws AccessDeniedException {
        if (user != null && !securityUtils.hasAccessByUserId(user.getUserId())) {
            throw new AccessDeniedException(ACCESS_DENIED_MESSAGE);
        }
    }

}
