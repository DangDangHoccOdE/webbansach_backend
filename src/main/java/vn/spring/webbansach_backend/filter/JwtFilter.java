package vn.spring.webbansach_backend.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import vn.spring.webbansach_backend.exception.JwtTokenExpiredException;
import vn.spring.webbansach_backend.service.IUserSecurityService;
import vn.spring.webbansach_backend.service.impl.JwtService;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {
    @Autowired
    private JwtService jwtService;
    @Autowired
    private IUserSecurityService iUserSecurityService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            authHeader = request.getHeader("X-Refresh-Token");
        }
        System.out.println("Header: "+authHeader);
        String token = null;
        String username = null;
        String tokenType=null;
        if (authHeader != null && (authHeader.startsWith("Bearer ") || authHeader.startsWith("Refresh-Token"))) {
            token = authHeader.substring(authHeader.startsWith("Bearer ")?7:14);
            tokenType = authHeader.startsWith("Bearer ")?JwtService.SECRET_ACCESS_TOKEN:JwtService.SECRET_REFRESH_TOKEN;

            try {
                username = jwtService.extractUserName(token, tokenType);
            } catch (JwtTokenExpiredException e) {
//                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//                response.setContentType("application/json");
//                response.getWriter().write("{\"error\": \"accessToken expired\"}");
//                return;
                throw e;
            }
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = iUserSecurityService.loadUserByUsername(username);

            boolean isTokenValid = jwtService.validateToken(token, userDetails, tokenType);
            if (isTokenValid) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}
