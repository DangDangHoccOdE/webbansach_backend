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
           "/user/activatedAccount",
           "/user/resendActivationCode",
            "/user/confirmChangeEmail/**",
            "/user/confirmForgotPassword",
            "/vouchers/search/findByIsAvailableAndIsActive/**",
            "/reviews/**",
            "review/getNumberOfStarReview/{bookId}",
            "/user/findUserByCondition/**"
    };
    public static final String[] PUBLIC_POST_ENDPOINTS={
        "/user/register",
        "/user/login",
         "/user/forgotPassword",
            "/user/passwordChange"

    };

    public static final String[] PUBLIC_PUT_ENDPOINTS={
            "/vouchers/updateIsActive/{voucherId}",
    };
    public static final String[] USER_POST_ENDPOINTS={
            "/user/refreshToken",
            "/wishList/addWishList",
            "/wishList/addBookToWishList",
            "/cart-items/addCartItem",
            "/vouchers/saveVoucherByUser",
            "/order/addOrder",
            "/review/addReviewOrder/{orderId}",
            "/payment/create-payment",

    };

    public static final String[] USER_DELETE_ENDPOINTS={
            "/wishList/deleteWishList/**",
            "/wishList/deleteBookOfWishList/**",
            "/cart-items/deleteCartItem/{cartItemId}",
            "/cart-items/deleteAllCartItemsIsChoose"
    };

    public static final String[] ADMIN_GET_ENDPOINTS={
        "/**",
    };
 public static final String[] USER_GET_ENDPOINTS={
        "/wishList/showWishList/**",
         "/wish-list/**",
         "/user/**",
         "/cart-items/**",
         "/vouchers/**",
         "/user-voucher/**",
         "/vouchers/findVoucherByVoucherCodeAndUserId/{code}/{userId}",
         "/vouchers/showVoucherByUserId/{userId}",
         "/orders/search/**",
         "/order/getBooksOfOrder/{orderId}",
         "/order-detail/getOrderDetailsFromOrderId/{orderId}",
         "/orders/**",
         "/payment/**",
         "/order-review/**",
         "/payment/payment-info",
         "/order-detail/search/findByOrder_OrderId",
         "/delivery/**"
 };

    public static final String[] USER_PUT_ENDPOINTS={
            "/user/changeInfo",
            "/user/changeEmail",
            "/wishList/editWishListName",
            "/cart-items/updateQuantityOfCartItem/{cartItemId}",
            "/order/cancelOrder/{oderId}",
            "/order/repurchase/{oderId}",
            "/order/confirmReceivedOrder/{oderId}",
            "/order/confirmSuccessfullyBankOrderPayment/{orderCode}",
            "/order/saveOrderStatusChange/{orderId}",
            "/review/editReviewOrder/**"
    };

    public static final String[] ADMIN_POST_ENDPOINTS={
        "/books/addBook/**",
        "/category/addCategory",
         "/vouchers/addVoucherAdmin",
         "/vouchers/giftVouchersToUsers",
         "/vouchers/addVouchersToVoucherAvailable",
    };

    public static final String[] ADMIN_PUT_ENDPOINTS={
        "/books/editBook/{bookId}",
         "/category/editCategory",
         "/vouchers/editVoucherAdmin/{voucherId}",
            "/review/hideReview/{reviewId}"

    };

    public static final String[] ADMIN_DELETE_ENDPOINTS={
        "/books/deleteBook/**",
        "/books/deleteBook/**",
        "/user/deleteUser/**",
        "/category/deleteCategory/**",
        "/category/*/books/*",
        "/vouchers/deleteVoucherAdmin/{voucherId}",
         "/vouchers/deleteVouchersSelected",
    };



}
