package com.example.sharding_demo;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TokenController {

    // Secret key for signing the JWT (for testing purposes, use a secure key in production)
    private String jwtSecret = "abcdefgabcdefgabcdefgabcdefgabcdefgabcdefg";

    Key signingKey = new SecretKeySpec(jwtSecret.getBytes(StandardCharsets.UTF_8), SignatureAlgorithm.HS256.getJcaName());

    // Token expiration time (e.g., 1 hour)
    private static final long EXPIRATION_TIME_MS = 3600000;

    @GetMapping("/generate-token")
    public String generateToken() {
        // Current timestamp
        long now = System.currentTimeMillis();

        // Generate the JWT token
        String token = Jwts.builder()
            .setSubject("user123")
            .claim("shop_id", 3) // Add custom claims
            .setIssuedAt(new Date(now)) // Token creation time
            .setExpiration(new Date(now + EXPIRATION_TIME_MS)) // Token expiration time
            .signWith(signingKey) // Sign the token with the secret key
            .compact();

        return token;
    }
}
