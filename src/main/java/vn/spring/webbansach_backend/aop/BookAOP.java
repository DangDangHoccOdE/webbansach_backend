package vn.spring.webbansach_backend.aop;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;
import vn.spring.webbansach_backend.dao.UserRepository;
import vn.spring.webbansach_backend.entity.User;
import vn.spring.webbansach_backend.utils.SecurityUtils;


@Aspect
@Component
public class BookAOP {
    private static final String ACCESS_DENIED_MESSAGE = "Bạn không có quyền truy cập!";
    private final SecurityUtils securityUtils;
    private final UserRepository userRepository;
    @Autowired
    public BookAOP(SecurityUtils securityUtils, UserRepository userRepository) {
        this.securityUtils = securityUtils;
        this.userRepository = userRepository;
    }

    @Before(value = "execution(* vn.spring.webbansach_backend.controller.BookController.getBooksInWishList(..)) && args(..,wishListId,page,size)", argNames = "wishListId,page,size")
    public void hasAccessByWishListId(int wishListId,int page,int size) throws AccessDeniedException {
        User user = userRepository.findUserByWishList_WishListId(wishListId);
        if (user != null && !securityUtils.hasAccessByUserId(user.getUserId())) {
            throw new AccessDeniedException(ACCESS_DENIED_MESSAGE);
        }
    }
}
