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
          "/remark/**",
           "/account/activatedAccount",
           "/account/resendActivationCode",
    };
    public static final String[] PUBLIC_POST_ENDPOINTS={
        "/account/register",
        "/account/login"
    };

    public static final String[] ADMIN_GET_ENDPOINTS={
        "/users",
        "/users/**",
    };
}
