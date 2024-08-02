package vn.spring.webbansach_backend.aop;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import vn.spring.webbansach_backend.dto.OrderDto;
import vn.spring.webbansach_backend.entity.Order;
import vn.spring.webbansach_backend.entity.User;
import vn.spring.webbansach_backend.service.inter.IOrderService;
import vn.spring.webbansach_backend.service.inter.IUserService;
import vn.spring.webbansach_backend.utils.SecurityUtils;

@Aspect
@Component
public class OrderAOP {
    private static final String ACCESS_DENIED_MESSAGE = "Bạn không có quyền truy cập!";

    private final IUserService iUserService;
    private final SecurityUtils securityUtils;
    private final IOrderService iOrderService;

    @Autowired
    public OrderAOP(IUserService iUserService, SecurityUtils securityUtils, IOrderService iOrderService) {
        this.iUserService = iUserService;
        this.securityUtils = securityUtils;
        this.iOrderService = iOrderService;
    }

    @Before(value = "(execution(* vn.spring.webbansach_backend.controller.OrderController.getBooksOfOrder(..))" +
            "||execution(* vn.spring.webbansach_backend.controller.OrderController.cancelOrder(..)))  && args(..,orderId)")
    public void checkAccess(Long orderId) throws AccessDeniedException{
        Order order = iOrderService.findOrderById(orderId);
        User user = order.getUser();

        checkAccessByUser(user);
    }

    @Before("execution(* vn.spring.webbansach_backend.controller.OrderController.addOrder(..)) && args(..,oderDto)")
    public void checkAccess(OrderDto oderDto) throws AccessDeniedException{
        User user = iUserService.findUserByUserId(oderDto.getUserId());

        checkAccessByUser(user);
    }

    private void checkAccessByUser(User user) throws AccessDeniedException {
        if (user != null && !securityUtils.hasAccessByUserId(user.getUserId())) {
            throw new AccessDeniedException(ACCESS_DENIED_MESSAGE);
        }
    }

}
