package vn.spring.webbansach_backend.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.HandlerExceptionResolver;
import vn.spring.webbansach_backend.exception.CustomAccessDeniedHandler;
import vn.spring.webbansach_backend.exception.JwtAuthenticationEntryPoint;
import vn.spring.webbansach_backend.filter.JwtFilter;
import vn.spring.webbansach_backend.service.IUserSecurityService;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    @Autowired
    private CustomBasicAuthenticationEntryPoint customBasicAuthenticationEntryPoint;

    @Autowired
    private CustomAccessDeniedHandler customAccessDeniedHandler;

    @Autowired
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver handlerExceptionResolver;

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

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
        httpSecurity.authorizeHttpRequests(
                        configurer->configurer
                                .requestMatchers(HttpMethod.GET,Endpoints.PUBLIC_GET_ENDPOINTS).permitAll()
                                .requestMatchers(HttpMethod.POST,Endpoints.PUBLIC_POST_ENDPOINTS).permitAll()
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
                        configuration.addAllowedOrigin((Endpoints.front_end_host)); // add resource can access app
                        configuration.setAllowedMethods(Arrays.asList("GET","POST","PUT","DELETE"));
                        configuration.addAllowedHeader("*"); // permission sth can access

                        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                        source.registerCorsConfiguration("/**",configuration);
                        return configuration;
                    });
                })
                .exceptionHandling(handling -> handling // Xử lý các lỗi 403 như @PreAuthorized
                        .accessDeniedHandler(customAccessDeniedHandler)
                )
                .addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class)
                .sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .httpBasic(httpBasic->httpBasic.authenticationEntryPoint(customBasicAuthenticationEntryPoint)) //Tùy chọn cách xác thực ngoại lệ đăng nhập
               .csrf(csrfConfigurer->csrfConfigurer.disable());

        return httpSecurity.build();
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception{
        return config.getAuthenticationManager(); // only authenticationManager in app
    }
}
