package com.example.authservice.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;

@Component
public class JWTTokenUtil {

    private final SecretKey signingKey;

    public JWTTokenUtil(@Value("${jwt.secret:}") String jwtSecret) {
        if (jwtSecret.isBlank()) {
            throw new IllegalStateException("JWT_SECRET must be set to the same Base64-encoded key in auth-service and gateway-service.");
        }
        signingKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    public String generateToken(String username) {
        Instant issuedAt = Instant.now();

        return Jwts.builder()
                .subject(username)
                .issuedAt(Date.from(issuedAt))
                .expiration(Date.from(issuedAt.plus(Duration.ofMinutes(15))))
                .signWith(signingKey, Jwts.SIG.HS256)
                .compact();
    }
}
