package com.Uber.User.Service.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class JwtUtils {

    @Value("${secret.key}")
    private String SECRET_KEY;

    public String generateToken(String username) {
        Map<String, String> claims = new HashMap<>();
        claims.put("Test claims", "claims 1");
        claims.put("Test Claims2", "claims 2");
        return createToken(claims, username);
    }

    private String createToken(Map<String, String> claims, String username) {
        return Jwts.builder()
                .claims(claims)
                .subject(username)
                .header().empty().add("type", "JWT")
                .and()
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 60 * 1000 * 5)) // 5 minutes
                .signWith(getSigningKey())
                .compact();
    }

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    public boolean validateJwt(String token, String expectedUsername) {
        try {
            Claims claims = extractClaims(token);
            String username = claims.getSubject();

            // Check if token is expired
            boolean isNotExpired = !isTokenExpired(token);

            // Check if username matches and token is not expired
            return username.equals(expectedUsername) && isNotExpired;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isTokenExpired(String token) {
        Date expiration = extractClaims(token).getExpiration();
        return expiration.before(new Date()); // Returns true if token is expired
    }

    public Claims extractClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}