package vn.spring.webbansach_backend.security;

public class Endpoints {
    public static final String front_end_host = "http://localhost:3000";
    public static final String[] PUBLIC_GET_ENDPOINTS={
        "/books",
         "/books/**",
         "/images",
         "/images/**",
         "/images/**",
          "/users/search/existsByEmail",
          "/users/search/existsByUserName",
          "/category/**",
          "/remarks/**",
           "/account/activatedAccount",
           "/account/resendActivationCode",
            "/users",
            "/users/**",
            "/user/confirmChangeEmail/**"
    };
    public static final String[] PUBLIC_POST_ENDPOINTS={
        "/user/register",
        "/user/login",
        "/user/refreshToken",

    };

    public static final String[] ADMIN_GET_ENDPOINTS={
        "/"
    };

    public static final String[] PUBLIC_PUT_ENDPOINTS={
            "/user/changeInfo",
            "/user/changeEmail",
    };

    public static final String[] ADMIN_POST_ENDPOINTS={
        "/admin/addBook",
        "/books",
        "/books/**"
    };



}
