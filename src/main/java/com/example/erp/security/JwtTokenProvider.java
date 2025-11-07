package com.example.erp.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import jakarta.annotation.PostConstruct;
import java.util.Date;
import java.util.List;

@Component
@Slf4j
public class JwtTokenProvider {
    
    @Value("${app.jwt.secret}")
    private String jwtSecret;
    
    @Value("${app.jwt.expiration}")
    private int jwtExpirationInMs;
    
    @PostConstruct
    public void init() {
        log.info("JWT Configuration:");
        log.info("  Secret key length: {} characters", jwtSecret != null ? jwtSecret.length() : 0);
        log.debug("  Secret key: {}... (truncated)", 
            jwtSecret != null ? jwtSecret.substring(0, Math.min(5, jwtSecret.length())) : "null");
        log.info("  Expiration: {} ms ({} hours)", jwtExpirationInMs, jwtExpirationInMs / (1000 * 60 * 60));
    }
    
    SecretKey getSigningKey() {
        try {
            // Ensure the key is properly encoded
            byte[] keyBytes = jwtSecret.getBytes(java.nio.charset.StandardCharsets.UTF_8);
            log.debug("Creating signing key with {} bytes", keyBytes.length);
            return Keys.hmacShaKeyFor(keyBytes);
        } catch (Exception e) {
            log.error("Error creating signing key: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to create signing key", e);
        }
    }
    
    public String generateToken(Authentication authentication) {
        try {
            log.info("Generating new JWT token...");
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            
            Date now = new Date();
            Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);
            
            log.debug("Token details:");
            log.debug("  User ID: {}", userPrincipal.getUserId());
            log.debug("  Username: {}", userPrincipal.getUsername());
            log.debug("  Issued at: {}", now);
            log.debug("  Expires at: {} (in {} ms)", expiryDate, jwtExpirationInMs);
            
            // Extract role names without ROLE_ prefix (will be added by Spring Security)
            List<String> roleNames = userPrincipal.getAuthorities().stream()
                    .map(auth -> {
                        String authority = auth.getAuthority();
                        // Remove ROLE_ prefix if present
                        String role = authority.startsWith("ROLE_") ? authority.substring(5) : authority;
                        log.debug("  Adding role to token: {}", role);
                        return role;
                    })
                    .collect(java.util.stream.Collectors.toList());
                    
            log.info("Generated JWT token for user {} with roles: {}", 
                userPrincipal.getUsername(), roleNames);
            
            String token = Jwts.builder()
                    .setSubject(Long.toString(userPrincipal.getUserId()))
                    .claim("roles", roleNames)
                    .setIssuedAt(now)
                    .setExpiration(expiryDate)
                    .signWith(getSigningKey())
                    .compact();
                    
            log.debug("Generated token (first 20 chars): {}...", 
                token.length() > 20 ? token.substring(0, 20) : token);
            return token;
            
        } catch (Exception e) {
            log.error("Error generating JWT token: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to generate JWT token", e);
        }
    }
    
    public Long getUserIdFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        
        return Long.parseLong(claims.getSubject());
    }
    
    public boolean validateToken(String authToken) {
        try {
            log.info("Validating JWT token");
            
            if (!StringUtils.hasText(authToken)) {
                log.warn("JWT token is null or empty");
                return false;
            }
            
            log.debug("Token length: {} characters", authToken.length());
            
            // Check if token is properly formatted (3 parts separated by .)
            String[] tokenParts = authToken.split("\\.");
            if (tokenParts.length != 3) {
                log.warn("JWT token is malformed - expected 3 parts, got {}", tokenParts.length);
                return false;
            }
            
            log.debug("Token has valid structure");
            
            // Parse the token with detailed error handling
            try {
                Jws<Claims> claimsJws = Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(authToken);
                    
                Claims claims = claimsJws.getBody();
                
                // Log token details
                log.info("JWT token validation successful");
                log.debug("Token details:");
                log.debug("  Subject (user ID): {}", claims.getSubject());
                log.debug("  Issued at: {}", claims.getIssuedAt());
                log.debug("  Expiration: {}", claims.getExpiration());
                log.debug("  Current time: {}", new Date());
                log.debug("  Token will expire in: {} ms", claims.getExpiration().getTime() - System.currentTimeMillis());
                log.debug("  Roles: {}", claims.get("roles"));
                
                // Check if token is expired
                if (claims.getExpiration().before(new Date())) {
                    log.warn("Token is expired: {}", claims.getExpiration());
                    log.warn("Current time: {}", new Date());
                    return false;
                }
                
                return true;
                
            } catch (ExpiredJwtException ex) {
                log.error("Token expired at: {}", ex.getClaims().getExpiration());
                log.error("Current time: {}", new Date());
                throw ex;
            } catch (MalformedJwtException ex) {
                log.error("Malformed JWT: {}", ex.getMessage());
                throw ex;
            } catch (SignatureException ex) {
                log.error("Invalid JWT signature: {}", ex.getMessage());
                log.debug("Expected key: {}... (truncated)", 
                    getSigningKey().toString().substring(0, Math.min(20, getSigningKey().toString().length())));
                throw ex;
            }
            
        } catch (SecurityException ex) {
            log.error("Invalid JWT signature - {}", ex.getMessage());
        } catch (MalformedJwtException ex) {
            log.error("Invalid JWT token - {}", ex.getMessage());
        } catch (ExpiredJwtException ex) {
            log.error("Expired JWT token - {}", ex.getMessage());
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT token - {}", ex.getMessage());
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty - {}", ex.getMessage());
        } catch (Exception ex) {
            log.error("Unexpected error validating JWT token - {}", ex.getMessage());
        }
        return false;
    }
}
