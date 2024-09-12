package vn.spring.webbansach_backend.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.HandlerExceptionResolver;
import vn.spring.webbansach_backend.filter.JwtFilter;
import vn.spring.webbansach_backend.oauth2.CustomOAuth2UserService;
import vn.spring.webbansach_backend.oauth2.HttpCookieOAuth2AuthorizationRequestRepository;
import vn.spring.webbansach_backend.oauth2.OAuth2AuthenticationFailureHandler;
import vn.spring.webbansach_backend.oauth2.OAuth2AuthenticationSuccessHandler;
import vn.spring.webbansach_backend.service.IUserSecurityService;
import vn.spring.webbansach_backend.service.UserSecurityService;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        securedEnabled = true,
        jsr250Enabled = true,
        prePostEnabled = true
)
public class SecurityConfiguration {
    @Autowired
    private CustomBasicAuthenticationEntryPoint customBasicAuthenticationEntryPoint;

    @Autowired
    private CustomAccessDeniedHandler customAccessDeniedHandler;

    @Autowired
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver handlerExceptionResolver;

    @Autowired
    private CustomOAuth2UserService customOAuth2UserService;

    @Autowired
    private HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;

    @Autowired
    private OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;

    @Autowired
    private OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;

    @Autowired
    private UserSecurityService userSecurityService;

    @Bean
    public JwtFilter jwtFilter(){
        return new JwtFilter(handlerExceptionResolver);
    }
    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Autowired
    public DaoAuthenticationProvider authenticationProvider(IUserSecurityService IUserSecurityService){
        DaoAuthenticationProvider dap = new DaoAuthenticationProvider();
        dap.setUserDetailsService(IUserSecurityService);
        dap.setPasswordEncoder(passwordEncoder());

        return dap;
    }

    /*
Theo mặc định, Spring OAuth2 sử dụng HttpSessionOAuth2AuthorizationRequestRepository để lưu
  yêu cầu ủy quyền. Tuy nhiên, vì dịch vụ của chúng tôi không có trạng thái nên chúng tôi không thể lưu nó trong
  phiên họp. Thay vào đó, chúng tôi sẽ lưu yêu cầu trong cookie được mã hóa Base64.
*/
    @Bean
    public HttpCookieOAuth2AuthorizationRequestRepository cookieAuthorizationRequestRepository() {
        return new HttpCookieOAuth2AuthorizationRequestRepository();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
        httpSecurity.authorizeHttpRequests(
                        configurer->configurer
                                .requestMatchers(HttpMethod.GET,Endpoints.PUBLIC_GET_ENDPOINTS).permitAll()
                                .requestMatchers(HttpMethod.POST,Endpoints.PUBLIC_POST_ENDPOINTS).permitAll()
                                .requestMatchers(HttpMethod.PUT,Endpoints.PUBLIC_PUT_ENDPOINTS).permitAll()
                                .requestMatchers(HttpMethod.GET,Endpoints.USER_GET_ENDPOINTS).hasAnyRole("USER","ADMIN")
                                .requestMatchers(HttpMethod.POST,Endpoints.USER_POST_ENDPOINTS).hasAnyRole("USER","ADMIN")
                                .requestMatchers(HttpMethod.PUT,Endpoints.USER_PUT_ENDPOINTS).hasAnyRole("USER","ADMIN")
                                .requestMatchers(HttpMethod.DELETE,Endpoints.USER_DELETE_ENDPOINTS).hasAnyRole("USER","ADMIN")
                                .requestMatchers(HttpMethod.GET,Endpoints.ADMIN_GET_ENDPOINTS).hasRole("ADMIN")
                                .requestMatchers(HttpMethod.POST,Endpoints.ADMIN_POST_ENDPOINTS).hasRole("ADMIN")
                                .requestMatchers(HttpMethod.PUT,Endpoints.ADMIN_PUT_ENDPOINTS).hasRole("ADMIN")
                                .requestMatchers(HttpMethod.DELETE,Endpoints.ADMIN_DELETE_ENDPOINTS).hasRole("ADMIN")
                )
                .cors(cors->{
                    cors.configurationSource(request -> {
                        CorsConfiguration configuration = new CorsConfiguration();
                        configuration.addAllowedOrigin((Endpoints.front_end_host)); // Thêm đươờng dẫn Frontend
                        configuration.setAllowedMethods(Arrays.asList("GET","POST","PUT","DELETE"));
                        configuration.addAllowedHeader("*"); // permission sth can access
                        configuration.setAllowCredentials(true);
                        configuration.setMaxAge(3600L);

                        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                        source.registerCorsConfiguration("/**",configuration);
                        return configuration;
                    });
                })
                .exceptionHandling(handling -> handling // Xử lý các lỗi 403 như @PreAuthorized, hasRole("Admin")
                        .accessDeniedHandler(customAccessDeniedHandler)
                )
                .addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class)
                .sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
                .httpBasic(httpBasic->httpBasic.authenticationEntryPoint(customBasicAuthenticationEntryPoint)) //Tùy chọn cách xác thực ngoại lệ đăng nhập
                .csrf(csrfConfigurer->csrfConfigurer.disable())
                .oauth2Login(oauth2 -> oauth2
                        .authorizationEndpoint(authorization -> authorization
                                .baseUri("/oauth2/authorize")  // URL endpoint xác thực OAuth2
                                .authorizationRequestRepository(cookieAuthorizationRequestRepository()) // Repository cho request OAuth2
                        )
                        .redirectionEndpoint(redirection -> redirection
                                .baseUri("/oauth2/callback/*")  // URL callback sau khi xác thực
                        )
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(customOAuth2UserService)  // Dịch vụ lấy thông tin người dùng OAuth2
                        )
                        .successHandler(oAuth2AuthenticationSuccessHandler)
                        .failureHandler(oAuth2AuthenticationFailureHandler)
                )
                .userDetailsService(userSecurityService); // Nếu không phải đăng nhập bằng oauth thì cái nay lấy thông tin người dùng

        return httpSecurity.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception{
        return config.getAuthenticationManager(); //
    }
}
