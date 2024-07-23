package vn.spring.webbansach_backend.security;

public class Endpoints {
    public static final String front_end_host = "http://localhost:3000";
    public static final String[] PUBLIC_GET_ENDPOINTS={
         "/books/**",
         "/images",
         "/images/**",
         "/images/**",
          "/users/search/existsByEmail",
          "/users/search/existsByUserName",
          "/category/**",
          "/feedbacks/**",
           "/account/activatedAccount",
           "/account/resendActivationCode",
            "/user/confirmChangeEmail/**",
            "/user/confirmForgotPassword",
    };
    public static final String[] PUBLIC_POST_ENDPOINTS={
        "/user/register",
        "/user/login",
         "/user/forgotPassword",
            "/user/passwordChange"

    };
    public static final String[] USER_POST_ENDPOINTS={
            "/user/refreshToken",
            "/wishList/addWishList",
            "/wishList/addBookToWishList",
            "/cart-items/addCartItem"
    };

    public static final String[] USER_DELETE_ENDPOINTS={
            "/wishList/deleteWishList/**",
            "/wishList/deleteBookOfWishList/**",
            "/cart-items/deleteCartItem/{cartItemId}"
    };

    public static final String[] ADMIN_GET_ENDPOINTS={
        "/",
         "/users/**"
    };
 public static final String[] USER_GET_ENDPOINTS={
        "/wishList/showWishList/**",
         "/wish-list/**",
         "/user/findUserByUsername/**",
         "/cart-items/**",
         "/vouchers/**"

 };

    public static final String[] USER_PUT_ENDPOINTS={
            "/user/changeInfo",
            "/user/changeEmail",
            "/wishList/editWishListName"
    };

    public static final String[] ADMIN_POST_ENDPOINTS={
        "/books/addBook/**",
        "/category/addCategory",
         "/vouchers/addVoucherAdmin",
         "/vouchers/giftVouchersToUsers",
    };

    public static final String[] ADMIN_PUT_ENDPOINTS={
        "/books/editBook/{bookId}",
         "/category/editCategory",
         "/vouchers/editVoucherAdmin/{voucherId}"

    };

    public static final String[] ADMIN_DELETE_ENDPOINTS={
        "/admin/deleteBook/**",
        "/user/deleteUser/**",
        "/category/deleteCategory/**",
        "/category/*/books/*",
        "/vouchers/deleteVoucherAdmin/{voucherId}",
         "/vouchers/deleteVouchersSelected"
    };



}
