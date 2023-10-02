package com.test.mytest.config;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtTokenUtil {

    private final String SECRET;
    private final long EXPIRATION_TIME;
    private static final String ISSUER = "echisan"; // Replace with your issuer

    private static final String ROLE_CLAIMS = "role";

    private static final long REMEMBER_ME_EXPIRATION_TIME = 604800L; // 7 days

    public JwtTokenUtil(@Value("${bezkoder.app.jwtSecret}") String secret,
                        @Value("${jwt.token.expiration-seconds}") long expirationTime) {
        this.SECRET = secret;
        this.EXPIRATION_TIME = expirationTime;
    }
    /**
     * token 過期時間
     */
    public long getExpirationTime(String token) {
        try {
            return getTokenBody(token).getExpiration().getTime();
        } catch (SignatureException ex) {
            return 0;
        }
    }
    /**
     * 
     * 判斷有沒有點選保持登入
     * 沒有1天
     * 有7天
     */
    public String createToken(String username, String role, boolean isRememberMe) {
        long expiration = isRememberMe ? REMEMBER_ME_EXPIRATION_TIME : EXPIRATION_TIME;

        Map<String, Object> claims = new HashMap<>();
        claims.put(ROLE_CLAIMS, role);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuer(ISSUER)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration ))
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .compact();
    }
    /**
     *從token 獲取account
     */
    public String getUsername(String token) {
        try {
            return getTokenBody(token).getSubject();
        } catch (SignatureException ex) {
            return null;
        }
    }
    /**
     * 從token 獲取role
     */
    public String getUserRole(String token) {
        try {
            return (String) getTokenBody(token).get(ROLE_CLAIMS);
        } catch (SignatureException ex) {
            return null;
        }
    }
    /**
     * 判斷token 是否過期
     */
    public boolean isTokenExpired(String token) {
        try {
            return getTokenBody(token).getExpiration().before(new Date());
        } catch (SignatureException ex) {
            return true;
        }
    }
    /**
     * 獲得token
     */
    public Claims getTokenBody(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET)
                .parseClaimsJws(token)
                .getBody();
    }
}