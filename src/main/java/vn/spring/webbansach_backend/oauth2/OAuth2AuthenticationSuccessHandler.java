package vn.spring.webbansach_backend.oauth2;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import vn.spring.webbansach_backend.config.AppProperties;
import vn.spring.webbansach_backend.entity.User;
import vn.spring.webbansach_backend.exception.BadRequestException;
import vn.spring.webbansach_backend.service.impl.JwtService;
import vn.spring.webbansach_backend.service.inter.IUserService;
import vn.spring.webbansach_backend.utils.CookieUtils;

import java.io.IOException;
import java.net.URI;
import java.util.Optional;

import static vn.spring.webbansach_backend.oauth2.HttpCookieOAuth2AuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME;

//Lớp OAuth2AuthenticationSuccessHandler thực hiện các chức năng chính sau:
//Xử lý sự kiện xác thực thành công bằng cách tạo JWT tokens và chuyển hướng người dùng.
//Xóa các thuộc tính xác thực và cookie liên quan để dọn dẹp sau khi xác thực.
//Kiểm tra tính hợp lệ của redirect URI để đảm bảo chỉ chuyển hướng đến các URI hợp lệ.
@Component
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final JwtService jwtService;
    private final AppProperties appProperties;
    private final IUserService iUserService;
    private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;


    @Autowired
    OAuth2AuthenticationSuccessHandler( JwtService jwtService, AppProperties appProperties,@Lazy IUserService iUserService,
                                       HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository) {
        this.jwtService = jwtService;
        this.appProperties = appProperties;
        this.iUserService = iUserService;
        this.httpCookieOAuth2AuthorizationRequestRepository = httpCookieOAuth2AuthorizationRequestRepository;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String targetUrl = determineTargetUrl(request, response, authentication);

        if (response.isCommitted()) {
            logger.debug("Phản hồi đã được cam kết. Không thể chuyển hướng đến " + targetUrl);
            return;
        }

        clearAuthenticationAttributes(request, response);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        Optional<String> redirectUri = CookieUtils.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
                .map(Cookie::getValue);

        // Check xem cookie có redirect_url k
        if(redirectUri.isPresent() && !isAuthorizedRedirectUri(redirectUri.get())) {
            throw new BadRequestException("Lấy làm tiếc! Chúng tôi có URI chuyển hướng trái phép và không thể tiến hành xác thực");
        }

        String targetUrl = redirectUri.orElse(getDefaultTargetUrl());
        final String accessToken = jwtService.generateToken(authentication.getName());
        final String refreshToken = jwtService.generateRefreshToken(authentication.getName());

        User user = iUserService.findUserByEmail(authentication.getName());

        user.setRefreshToken(refreshToken);
        iUserService.saveUser(user);

        // ĐƯờng dẫn trả về về là http://localhost:3000/oauth2/redirect?accessToken=...&refreshToken=...
        return UriComponentsBuilder.fromUriString(targetUrl)
                .queryParam("accessToken", accessToken)
                .queryParam("refreshToken",refreshToken)
                .build().toUriString();
    }

    protected void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        httpCookieOAuth2AuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
    }

    private boolean isAuthorizedRedirectUri(String uri) {
        URI clientRedirectUri = URI.create(uri);

        return appProperties.getOauth2().getAuthorizedRedirectUris()
                .stream()
                .anyMatch(authorizedRedirectUri -> {
                    //Chỉ xác nhận máy chủ và cổng
                    URI authorizedURI = URI.create(authorizedRedirectUri);
                    return authorizedURI.getHost().equalsIgnoreCase(clientRedirectUri.getHost())
                            && authorizedURI.getPort() == clientRedirectUri.getPort();
                });
    }
}
