package vn.spring.webbansach_backend.service.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import vn.spring.webbansach_backend.entity.Role;
import vn.spring.webbansach_backend.entity.User;
import vn.spring.webbansach_backend.service.inter.IUserService;
import java.util.*;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtService {
    public static final String SECRET = "dsafwerwdrwerwer01234567894234234rfsdfsdfsdfsdvfuyuytuytu0123456789";
    private final long JWT_EXPIRATION = 604800000L;
    @Autowired
    private IUserService iUserService;

    // Generate token from to username
    public String generateToken(String userName){
        Map<String, Object> claim = new HashMap<>();
        User user = iUserService.findByUserName(userName);
        claim.put("avatar", user.getAvatar());
        claim.put("enable",user.isActive() );
        claim.put("firstName",user.getFirstName());
        claim.put("userId",user.getUserId());

        boolean isAdmin = false;
        boolean isStaff = false;
        boolean isUser = false;

       List<Role> list = user.getRoleList();
       if(!list.isEmpty()) {
           for (Role role : list) {
               if (role.getRoleName().equals("ROLE_ADMIN")) {
                   isAdmin = true;
                   break;
               }
               if (role.getRoleName().equals("ROLE_STAFF")) {
                   isStaff = true;
                    break;
               }
               if (role.getRoleName().equals("ROLE_USER")) {
                   isUser = true;
                   break;
               }
           }
       }
        claim.put("isAdmin",isAdmin);
        claim.put("isStaff",isStaff);
        claim.put("isUser",isUser);

        return createToken(claim,userName);
    }

    // create jwt with claim
    private String createToken(Map<String,Object> claim, String userName){
        return Jwts.builder()
                .setClaims(claim)
                .setSubject(userName)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+ JWT_EXPIRATION)) // expire JWT 7 days
                .signWith(getSignKey(),SignatureAlgorithm.HS256)
                .compact(); // pack then return jwt builder
    }

    private Key getSignKey(){
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
        // check length from keyBytes then choose a SignatureAlgorithm from hmac-sha, return a secretKey
    }

    // extract info
    private Claims extractAllClaims(String token){
        return Jwts.parser().setSigningKey(getSignKey()).parseClaimsJws(token).getBody();
    }

    // extract info to 1 claims
    public <T> T extractClaims(String token, Function<Claims,T> claimsFunction){
        final Claims claims = extractAllClaims(token);
        return claimsFunction.apply(claims);
    }

    // check time expire
    public Date extractExpiration(String token){
        try {
            return extractClaims(token, Claims::getExpiration);
        } catch (JwtException e) {
            throw new RuntimeException("Failed to extract expiration from JWT: " + e.getMessage());
        }
    }

    // check username
    public String extractUserName(String token){
        try {
            return extractClaims(token, Claims::getSubject);
        } catch (JwtException e) {
            throw new RuntimeException("Failed to extract username", e);
        }
    }
    // check jwt
    private Boolean isTokenExpired(String token){
        Date expiration = extractExpiration(token);
        if(expiration==null){
            throw new RuntimeException("Expiration date is null");
        }
        return expiration.before(new Date());
    }

    // check token valid
    public Boolean validateToken(String token, UserDetails userDetails){
        final String userName = extractUserName(token);
        return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
