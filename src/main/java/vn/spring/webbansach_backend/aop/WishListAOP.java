package vn.spring.webbansach_backend.aop;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;
import vn.spring.webbansach_backend.dao.UserRepository;
import vn.spring.webbansach_backend.dao.WishListRepository;
import vn.spring.webbansach_backend.dto.WishListDto;
import vn.spring.webbansach_backend.entity.User;
import vn.spring.webbansach_backend.entity.WishList;
import vn.spring.webbansach_backend.utils.SecurityUtils;

import java.util.Map;

@Aspect
@Component
public class WishListAOP {

    private static final String ACCESS_DENIED_MESSAGE = "Bạn không có quyền truy cập!";

    @Autowired
    private SecurityUtils securityUtils;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private WishListRepository wishListRepository;

    @Before("execution(* vn.spring.webbansach_backend.controller.WishListController.showWishList(..)) && args(..,userId)")
    public void hasAccessByUserId(Long userId) throws AccessDeniedException {
        checkAccessByUserId(userId);
    }

    @Before("execution(* vn.spring.webbansach_backend.controller.WishListController.deleteWishList(..)) && args(..,wishListId)")
    public void hasAccessByWishListId(int wishListId) throws AccessDeniedException {
        User user = userRepository.findUserByWishList_WishListId(wishListId);
        checkAccessByUser(user);
    }

    @Before("(execution(* vn.spring.webbansach_backend.controller.WishListController.addWishList(..))" +
            "|| execution(* vn.spring.webbansach_backend.controller.WishListController.editWishListName(..))) && args(..,wishListDto)")
    public void hasAccessByWishListDto(WishListDto wishListDto) throws AccessDeniedException {
        WishList wishList = wishListRepository.findByWishListId(wishListDto.getWishListId());
        if (wishList != null) {
            checkAccessByUser(wishList.getUser());
        }
    }

    @Before("(execution(* vn.spring.webbansach_backend.controller.WishListController.addBookToWishList(..))" +
            "|| execution(* vn.spring.webbansach_backend.controller.WishListController.deleteBookOfWishList(..))) && args(..,map)")
    public void hasAccessByMap(Map<String, Integer> map) throws AccessDeniedException {
        WishList wishList = wishListRepository.findByWishListId(map.get("wishListId"));
        if (wishList != null) {
            checkAccessByUser(wishList.getUser());
        }
    }

    private void checkAccessByUserId(Long userId) throws AccessDeniedException {
        if (!securityUtils.hasAccessByUserId(userId)) {
            throw new AccessDeniedException(ACCESS_DENIED_MESSAGE);
        }
    }

    private void checkAccessByUser(User user) throws AccessDeniedException {
        if (user != null && !securityUtils.hasAccessByUserId(user.getUserId())) {
            throw new AccessDeniedException(ACCESS_DENIED_MESSAGE);
        }
    }
}
