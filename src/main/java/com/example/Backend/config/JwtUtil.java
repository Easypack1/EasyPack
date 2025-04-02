package com.example.Backend.config;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    private static final String SECRET_KEY = "EasyPackSecretKeyEasyPackSecretKeyEasyPackSecretKey123!";
    private static final long EXPIRATION_TIME = 1000 * 60 * 60; // 1시간

    private final Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

    // JWT 생성
    public String generateToken(String userId) {
        return Jwts.builder()
                .setSubject(userId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // JWT에서 클레임 추출
    public Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // ✅ 추가: userId 추출
    public String extractUserId(String token) {
        return extractClaims(token).getSubject();
    }

    // JWT 유효성 검증
    public boolean isTokenValid(String token) {
        try {
            extractClaims(token); // 유효하지 않으면 예외 발생
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}
