package com.example.Backend.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.JacksonDeserializer;
import io.jsonwebtoken.io.JacksonSerializer;
import io.jsonwebtoken.io.Serializer;
import io.jsonwebtoken.io.Deserializer;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.Map;

@Component
public class JwtUtil {

    private static final String SECRET_KEY = "EasyPackSecretKeyEasyPackSecretKeyEasyPackSecretKey123!";
    private static final long EXPIRATION_TIME = 1000 * 60 * 60; // 1시간

    private final Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));

    private final JwtParser parser = Jwts.parserBuilder()
            .deserializeJsonWith(new JacksonDeserializer<>(new ObjectMapper()))  // ✅ JSON 파싱기
            .setSigningKey(key)
            .build();

    private final Serializer<Map<String, ?>> serializer = new JacksonSerializer<>(new ObjectMapper());

    // JWT 생성
    public String generateToken(String userId) {
        return Jwts.builder()
                .setSubject(userId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key, SignatureAlgorithm.HS256)
                .serializeToJsonWith(serializer)  // ✅ JSON 생성기
                .compact();
    }

    // Claims 추출
    public Claims extractClaims(String token) {
        return parser.parseClaimsJws(token).getBody();
    }

    // userId 추출
    public String extractUserId(String token) {
        return extractClaims(token).getSubject();
    }

    // 토큰 유효성 검사
    public boolean isTokenValid(String token) {
        try {
            extractClaims(token); // 예외 발생 시 유효하지 않음
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    // 토큰 검증 및 userId 반환
    public String validateTokenAndGetUserId(String token) {
        if (!isTokenValid(token)) {
            throw new RuntimeException("유효하지 않은 토큰입니다.");
        }
        return extractUserId(token);
    }
}
