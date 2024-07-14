package vn.spring.webbansach_backend.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import vn.spring.webbansach_backend.entity.Notice;

import java.io.IOException;
import java.security.SignatureException;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");

        String message = "Xác thực không thành công";
        if (authException.getCause() instanceof ExpiredJwtException) {
            message = "Token đã hết hạn";
        } else if (authException.getCause() instanceof UnsupportedJwtException) {
            message = "Token không được hỗ trợ";
        } else if (authException.getCause() instanceof MalformedJwtException) {
            message = "Token không đúng định dạng";
        } else if (authException.getCause() instanceof SignatureException) {
            message = "Chữ ký token không hợp lệ";
        } else if (authException.getCause() instanceof IllegalArgumentException) {
            message = "Token không hợp lệ";
        }

        ObjectMapper mapper = new ObjectMapper();
        String jsonResponse = mapper.writeValueAsString(new Notice(message));
        response.getWriter().write(jsonResponse);
    }

}