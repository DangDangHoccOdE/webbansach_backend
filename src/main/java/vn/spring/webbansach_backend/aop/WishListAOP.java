package vn.spring.webbansach_backend.aop;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;
import vn.spring.webbansach_backend.dao.UserRepository;
import vn.spring.webbansach_backend.dto.WishListDto;
import vn.spring.webbansach_backend.entity.User;
import vn.spring.webbansach_backend.utils.SecurityUtils;

@Aspect
@Component
public class WishListAOP {
    @Autowired
    private SecurityUtils securityUtils;
    @Autowired
    private UserRepository userRepository;

    @Before("execution(* vn.spring.webbansach_backend.controller.WishListController.showWishList(..)) && args(..,userId)")
    public void hasAccess(Long userId) throws AccessDeniedException{
        if (!securityUtils.hasAccessByUserId(userId)) {
            System.out.println("Không có quyền truy cập!");
            throw new AccessDeniedException("Bạn không có quyền truy cập!");
        }
    }

    @Before("execution(* vn.spring.webbansach_backend.controller.WishListController.deleteWishList(..)) && args(..,wishListId)")
    public void hasAccess(int wishListId) throws AccessDeniedException{
        User user = userRepository.findUserByWishList_WishListId(wishListId);
        if (!securityUtils.hasAccessByUserId(user.getUserId())) {
            System.out.println("Không có quyền truy cập!");
            throw new AccessDeniedException("Bạn không có quyền truy cập!");
        }
    }

    @Before("execution(* vn.spring.webbansach_backend.controller.WishListController.addWishList(..)) && args(..,wishListDto)")
    public void hasAccess(WishListDto wishListDto) throws AccessDeniedException{
        User user = userRepository.findByUserId(wishListDto.getUserId());
        if (!securityUtils.hasAccessByUserId(user.getUserId())) {
            System.out.println("Không có quyền truy cập!");
            throw new AccessDeniedException("Bạn không có quyền truy cập!");
        }
    }

}
