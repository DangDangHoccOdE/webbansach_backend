package vn.spring.webbansach_backend.aop;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;
import vn.spring.webbansach_backend.entity.Order;
import vn.spring.webbansach_backend.entity.User;
import vn.spring.webbansach_backend.service.inter.IOrderService;
import vn.spring.webbansach_backend.utils.SecurityUtils;

@Component
@Aspect
public class OrderDetailAOP {
    private static final String ACCESS_DENIED_MESSAGE = "Bạn không có quyền truy cập!";

    private final SecurityUtils securityUtils;
    private final IOrderService iOrderService;
    @Autowired
    public OrderDetailAOP(SecurityUtils securityUtils, IOrderService iOrderService) {
        this.securityUtils = securityUtils;
        this.iOrderService = iOrderService;
    }

    @Before(value = "(execution(* vn.spring.webbansach_backend.controller.OrderDetailController.getOrderDetailsFromOrderId(..)) && args(..,orderId))")
    public void hasAccess(Long orderId) throws AccessDeniedException {
        Order order = iOrderService.findOrderById(orderId);
        if(order!=null){
            User user = order.getUser();
            checkAccessByUser(user);
        }
    }

    private void checkAccessByUser(User user) throws AccessDeniedException {
        if (user != null && !securityUtils.hasAccessByUserId(user.getUserId())) {
            throw new AccessDeniedException(ACCESS_DENIED_MESSAGE);
        }
    }
}
