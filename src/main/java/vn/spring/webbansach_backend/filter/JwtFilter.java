package vn.spring.webbansach_backend.filter;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;
import vn.spring.webbansach_backend.service.IUserSecurityService;
import vn.spring.webbansach_backend.service.impl.JwtService;

import java.io.IOException;
public class JwtFilter extends OncePerRequestFilter {
    private final HandlerExceptionResolver resolver;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private IUserSecurityService iUserSecurityService;
    public JwtFilter(@Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolver) {
        this.resolver = resolver;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String authHeader = request.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                authHeader = request.getHeader("X-Refresh-Token");
            }
            System.out.println("Header: " + authHeader);
            String token = null;
            String username = null;
            String tokenType = null;
            if (authHeader != null && (authHeader.startsWith("Bearer ") || authHeader.startsWith("Refresh-Token"))) {
                token = authHeader.substring(authHeader.startsWith("Bearer ") ? 7 : 14);
                tokenType = authHeader.startsWith("Bearer ") ? JwtService.SECRET_ACCESS_TOKEN : JwtService.SECRET_REFRESH_TOKEN;

                username = jwtService.extractUserName(token, tokenType);
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
        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException | IllegalArgumentException e) {
            resolver.resolveException(request,response,null,e);
    }
    }
}
