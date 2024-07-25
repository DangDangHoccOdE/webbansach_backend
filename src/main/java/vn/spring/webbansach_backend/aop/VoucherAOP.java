package vn.spring.webbansach_backend.aop;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;
import vn.spring.webbansach_backend.utils.SecurityUtils;

import java.util.Map;

@Aspect
@Component
public class VoucherAOP {
    private final SecurityUtils securityUtils;

    @Autowired
    public VoucherAOP(SecurityUtils securityUtils){
        this.securityUtils = securityUtils;
    }

    @Before("execution(* vn.spring.webbansach_backend.controller.VoucherController.saveVoucherByUser(..)) && args(..,map)")
    public void checkAccess(Map<String,Integer> map) throws AccessDeniedException {
        long userId = map.get("userId");

        if (!securityUtils.hasAccessByUserId(userId)){
            throw new AccessDeniedException("Bạn không có quyền truy cập!");
        }
    }
}
