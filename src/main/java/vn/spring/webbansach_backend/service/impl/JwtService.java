package vn.spring.webbansach_backend.service.impl;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import vn.spring.webbansach_backend.entity.Role;
import vn.spring.webbansach_backend.entity.User;
import vn.spring.webbansach_backend.service.IUserSecurityService;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtService {
    public static final String SECRET_ACCESS_TOKEN = "dsafwerwdrwerwer01234567894234234rfsdfsdfsdfsdvfuyuytuytu0123456789";
    public static final String SECRET_REFRESH_TOKEN = "EUOIQWEUQWEJQOWIDKJASDNASDNM1231290KCMASKMDQSLKMDK323I912I3KMCADASD";
    private final long ACCESS_TOKEN_EXPIRATION = 2*60 * 60 * 1000; // 2 hours
    private final long REFRESH_TOKEN_EXPIRATION = 7*24*60 * 60 * 1000; // 7 dáys

    private final IUserSecurityService iUserSecurityService;

    @Autowired
    public JwtService(IUserSecurityService iUserSecurityService) {
        this.iUserSecurityService = iUserSecurityService;
    }

    public String generateToken(String userName) {
        Map<String, Object> claim = new HashMap<>();
        User user = iUserSecurityService.findByUserName(userName);

        if(user == null){
            user = iUserSecurityService.findByEmail(userName); // Trường hợp đăng nhập bằng gmail
        }
        claim.put("enable", user.isActive());
        claim.put("userId", user.getUserId());
        claim.put("type","accessToken");

        boolean isAdmin = false;
        boolean isStaff = false;
        boolean isUser = false;

        List<Role> list = user.getRoleList();
        if (!list.isEmpty()) {
            for (Role role : list) {
                if (role.getRoleName().equals("ROLE_ADMIN")) {
                    isAdmin = true;
                    break;
                }
                if (role.getRoleName().equals("ROLE_USER")) {
                    isUser = true;
                    break;
                }
            }
        }
        claim.put("isAdmin", isAdmin);
        claim.put("isUser", isUser);

        return createToken(claim, userName, ACCESS_TOKEN_EXPIRATION, SECRET_ACCESS_TOKEN);
    }

    private String createToken(Map<String, Object> claim, String userName, long expiration, String secret) {
        return Jwts.builder()
                .setClaims(claim)
                .setSubject(userName)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignKey(secret), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("type","refreshToken");
        return createToken(claims, username, REFRESH_TOKEN_EXPIRATION, SECRET_REFRESH_TOKEN);
    }

    private Key getSignKey(String secret) {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private Claims extractAllClaims(String token, String secret) {
            return Jwts.parser().setSigningKey(getSignKey(secret)).parseClaimsJws(token).getBody();
    }

    public <T> T extractClaims(String token, Function<Claims, T> claimsFunction, String secret) {
        Claims claims = extractAllClaims(token, secret);
        return claimsFunction.apply(claims);
    }

    public Date extractExpiration(String token, String secret) {
        try {
            return extractClaims(token, Claims::getExpiration, secret);
        } catch (JwtException e) {
            throw new RuntimeException("Failed to extract expiration from JWT: " + e.getMessage());
        }
    }

    public String extractUserName(String token, String secret) {
            return extractClaims(token, Claims::getSubject, secret);
    }

    private Boolean isTokenExpired(String token, String secret) {
        Date expiration = extractExpiration(token, secret);
        if (expiration == null) {
            throw new RuntimeException("Expiration date is null");
        }
        return expiration.before(new Date());
    }

    public Boolean validateToken(String token, UserDetails userDetails, String secret) {
         String userName = extractUserName(token, secret);
        return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token, secret));
    }

    public Boolean validateRefreshToken(String refreshToken, String secret) {
        try {
            extractAllClaims(refreshToken, secret);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}
