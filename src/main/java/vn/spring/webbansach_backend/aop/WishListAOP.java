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
    @Autowired
    private SecurityUtils securityUtils;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private WishListRepository wishListRepository;

    @Before("execution(* vn.spring.webbansach_backend.controller.WishListController.showWishList(..)) && args(..,userId)")
    public void hasAccess(Long userId) throws AccessDeniedException{
        if (!securityUtils.hasAccessByUserId(userId)) {
            throw new AccessDeniedException("Bạn không có quyền truy cập!");
        }
    }

    @Before("execution(* vn.spring.webbansach_backend.controller.WishListController.deleteWishList(..)) && args(..,wishListId)")
    public void hasAccess(int wishListId) throws AccessDeniedException{
        User user = userRepository.findUserByWishList_WishListId(wishListId);
        if (!securityUtils.hasAccessByUserId(user.getUserId())) {
            throw new AccessDeniedException("Bạn không có quyền truy cập!");
        }
    }

    @Before("(execution(* vn.spring.webbansach_backend.controller.WishListController.addWishList(..))" +
            "|| execution(* vn.spring.webbansach_backend.controller.WishListController.editWishListName(..))) && args(..,wishListDto)")
    public void hasAccess(WishListDto wishListDto) throws AccessDeniedException{
        WishList wishList = wishListRepository.findByWishListId(wishListDto.getWishListId());
        if(wishList!=null){
            User user = wishList.getUser();
            if (!securityUtils.hasAccessByUserId(user.getUserId())) {
                throw new AccessDeniedException("Bạn không có quyền truy cập!");
            }
        }

    }

    @Before("(execution(* vn.spring.webbansach_backend.controller.WishListController.addBookToWishList(..))"+
            "|| execution(* vn.spring.webbansach_backend.controller.WishListController.deleteBookOfWishList(..))) && args(..,map)")
    public void hasAccess(Map<String,Integer> map) throws AccessDeniedException {
        WishList wishList = wishListRepository.findByWishListId(map.get("wishListId"));
        if (wishList != null) {
            User user = wishList.getUser();
            if (!securityUtils.hasAccessByUserId(user.getUserId())) {
                throw new AccessDeniedException("Bạn không có quyền truy cập!");
            }
        }
    }



}
