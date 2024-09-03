package vn.spring.webbansach_backend.aop;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;
import vn.spring.webbansach_backend.dao.UserRepository;
import vn.spring.webbansach_backend.dto.CartItemDto;
import vn.spring.webbansach_backend.entity.CartItem;
import vn.spring.webbansach_backend.entity.User;
import vn.spring.webbansach_backend.service.inter.ICartItemService;
import vn.spring.webbansach_backend.utils.SecurityUtils;

import java.util.Map;
import java.util.*;

@Aspect
@Component
public class CartItemAOP {
    private static final String ACCESS_DENIED_MESSAGE = "Bạn không có quyền truy cập!";

    private final SecurityUtils securityUtils;
    private final UserRepository userRepository;
    private final ICartItemService iCartItemService;

    @Autowired
    public CartItemAOP(SecurityUtils securityUtils, UserRepository userRepository, ICartItemService iCartItemService) {
        this.securityUtils = securityUtils;
        this.userRepository = userRepository;
        this.iCartItemService = iCartItemService;
    }
    @Before(value = "execution(* vn.spring.webbansach_backend.controller.CartItemController.findCartItemsByUserId(..)) && args(..,userId,page,size)", argNames = "userId,page,size")
    public void hasAccessByUsername(Long userId,int page,int size) throws AccessDeniedException {
        User user = userRepository.findByUserId(userId);
        if (user != null && !securityUtils.hasAccessByUserId(user.getUserId())) {
            throw new AccessDeniedException(ACCESS_DENIED_MESSAGE);
        }
    }

    @Before("execution(* vn.spring.webbansach_backend.controller.CartItemController.addCartItem(..)) && args(..,cartItemDto)")
    public void hasAccessByUsername(CartItemDto cartItemDto) throws AccessDeniedException {
        User user = userRepository.findByUserId(cartItemDto.getUserId());
        if (user != null && !securityUtils.hasAccessByUserId(user.getUserId())) {
            throw new AccessDeniedException(ACCESS_DENIED_MESSAGE);
        }
    }

    @Before(value = "execution(* vn.spring.webbansach_backend.controller.CartItemController.findCartItemsByUserId(..)) && args(..,userId,page,size,sort,direction)", argNames = "userId,page,size,sort,direction")
    public void hasAccessByUsername(Long userId,int page, int size, String sort,String direction) throws AccessDeniedException {
        User user = userRepository.findByUserId(userId);
        if (user != null && !securityUtils.hasAccessByUserId(user.getUserId())) {
            throw new AccessDeniedException(ACCESS_DENIED_MESSAGE);
        }
    }
    @Before(value = "execution(* vn.spring.webbansach_backend.controller.CartItemController.updateQuantityOfCartItem(..)) && args(..,cartItemId,map)", argNames = "cartItemId,map")
    public void hasAccessByUsername(Long cartItemId,Map<String,Integer> map) throws AccessDeniedException {
        CartItem cartItem = iCartItemService.findCartItemById(cartItemId);
        User user = cartItem.getUser();
        if (user != null && !securityUtils.hasAccessByUserId(user.getUserId())) {
            throw new AccessDeniedException(ACCESS_DENIED_MESSAGE);
        }
    }
    @Before("execution(* vn.spring.webbansach_backend.controller.CartItemController.deleteBooksOfCart(..)) && args(..,cartItemId)")
    public void hasAccessByUsername(Long cartItemId) throws AccessDeniedException {
        CartItem cartItem = iCartItemService.findCartItemById(cartItemId);
        User user = cartItem.getUser();
        if (user != null && !securityUtils.hasAccessByUserId(user.getUserId())) {
            throw new AccessDeniedException(ACCESS_DENIED_MESSAGE);
        }
    }

    @Before("execution(* vn.spring.webbansach_backend.controller.CartItemController.deleteAllCartItemsIsChoose(..)) && args(..,allCartItemsIsChoose)")
    public void hasAccessByUsername(List<Long> allCartItemsIsChoose) throws AccessDeniedException {
        for(Long cartItemId:allCartItemsIsChoose) {
            CartItem cartItem = iCartItemService.findCartItemById(cartItemId);
            User user = cartItem.getUser();
            if (user != null && !securityUtils.hasAccessByUserId(user.getUserId())) {
                throw new AccessDeniedException(ACCESS_DENIED_MESSAGE);
            }
        }
    }
}
