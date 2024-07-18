package vn.spring.webbansach_backend.aop;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;
import vn.spring.webbansach_backend.dao.UserRepository;
import vn.spring.webbansach_backend.dto.CartItemDto;
import vn.spring.webbansach_backend.entity.User;
import vn.spring.webbansach_backend.service.inter.IUserService;
import vn.spring.webbansach_backend.utils.SecurityUtils;

@Aspect
@Component
public class CartItemAOP {
    private static final String ACCESS_DENIED_MESSAGE = "Bạn không có quyền truy cập!";

    private final SecurityUtils securityUtils;
    private final UserRepository userRepository;

    @Autowired
    public CartItemAOP(SecurityUtils securityUtils, UserRepository userRepository) {
        this.securityUtils = securityUtils;
        this.userRepository = userRepository;
    }

    @Before("execution(* vn.spring.webbansach_backend.controller.CartItemController.addCartItem(..)) && args(..,cartItemDto)")
    public void hasAccessByUsername(CartItemDto cartItemDto) throws AccessDeniedException {
        User user = userRepository.findByUserId(cartItemDto.getUserId());
        if (user != null && !securityUtils.hasAccessByUserId(user.getUserId())) {
            throw new AccessDeniedException(ACCESS_DENIED_MESSAGE);
        }
    }
}
