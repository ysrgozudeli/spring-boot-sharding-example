package com.example.sharding_demo.sharding.request_data;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@Slf4j
public class ShopIdInterceptor implements HandlerInterceptor {

    @Value("${jwt.secret}") // Load secret key from application properties
    private String jwtSecret;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // Extract shopId from query parameters, headers, or body
        Long shopId = extractShopId(request);

        ShopContext.setShopId(shopId);

        return true;
    }

    private Long extractShopId(HttpServletRequest request) {

        // Check query params
        String shopIdParam = request.getParameter("shopId");
        if (shopIdParam != null) {
            return Long.parseLong(shopIdParam);
        }

        // Check headers
        String shopIdHeader = request.getHeader("X-Shop-Id");
        if (shopIdHeader != null) {
            return Long.parseLong(shopIdHeader);
        }

        // Check JWT token in Authorization header
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            return extractShopIdFromJwt(token);
        }

        // Check body
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode     body         = objectMapper.readTree(request.getInputStream());
            if (body.has("shopId")) {
                return body.get("shopId").asLong();
            }
        } catch (IOException e) {
            // Log an error if needed and handle the exception
            log.warn("shop id not found in the request: " + e.getMessage());
            return null;
        }
        return null;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {

        ShopContext.clear(); // Clean up after the request
    }

    private Long extractShopIdFromJwt(String token) {

        // TODO: sometimes we have more than one shop_id in one token
        //  however we assume all of them are in the same sharded server
        /*
        * {
  "iat": 1736794139,
  "exp": 1736880539,
  "jti": "gokhan",
  "authorizations": [
    "organisation.admin",
    "shop.order.manager",
    "shop.ticket.viewer",
    "shop.baseproduct.user",
    "shop.landingpage.manager",
    "shop.webshop.manager",
    "shop.referenceshop.manager",
    "shop.product.manager",
    "shop.client.manager",
    "shop.gloryinventory.viewer",
    "shop.repairsubsidy.user",
    "shop.invoice.viewer"
  ],
  "authorized-shops": [
    1,
    75,
    76,
    72,
    68,
    50,
    71,
    83,
    122
  ]
}
        * */
        try {
            // Create the signing key using the secret
            Key key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));

            // Build the JWT parser and parse the token
            Claims claims = Jwts.parserBuilder()
                .setSigningKey(key) // Set the signing key
                .build()
                .parseClaimsJws(token) // Parse the JWT
                .getBody();

            // Extract shopId from claims
            if (claims.containsKey("shop_id")) {
                return claims.get("shop_id", Long.class);
            } else {
                log.warn("shopId not found in JWT claims.");
            }
        } catch (Exception e) {
            log.warn("Failed to parse JWT token: {}", e.getMessage());
        }
        return null;
    }
}
