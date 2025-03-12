package org.example.test_orm.service;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;



@Component
public class TokenService {
    @Value("${JWT_SECRET}")
    private String secret;
    @Value("${JWT_ACCESS_EXPIRATION}")
    private long accessToken;
    @Value("${JWT_REFRESH_EXPIRATION}")
    private long refreshToken;

    private SecretKey key;

    @PostConstruct
    public void init(){
        key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateAccessToken(String username) {
        return generateToken(username,  accessToken * 1000);    // Умножение для преобразования времени в секунды
    }

    public String generateRefreshToken(String username) {
        return generateToken(username, refreshToken * 1000);    // Умножение для преобразования времени в секунды
    }

     private String generateToken(String userID, long expiration) {
        return Jwts.builder()
                .subject(userID)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(key)
                .compact();
    }

    public String parseToken(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getSubject();
        }   catch (ExpiredJwtException e) {
            throw new ExpiredJwtException(e.getHeader(), e.getClaims(), e.getMessage());
        }   catch (JwtException e) {
            throw new JwtException(e.getMessage(), e);
        }
    }
}