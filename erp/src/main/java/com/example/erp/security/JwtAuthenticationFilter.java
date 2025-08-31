package com.example.erp.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import java.util.stream.Collectors;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.io.IOException;
import java.util.List;

@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    
    private final JwtTokenProvider tokenProvider;
    private final CustomUserDetailsService userDetailsService;
    
    public JwtAuthenticationFilter(JwtTokenProvider tokenProvider, CustomUserDetailsService userDetailsService) {
        this.tokenProvider = tokenProvider;
        this.userDetailsService = userDetailsService;
    }
    
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        // Only exclude endpoints that don't need JWT processing at all
        return path.startsWith("/api/auth/") || 
               path.startsWith("/actuator/health") ||
               path.startsWith("/h2-console/") ||
               path.startsWith("/swagger-ui/") ||
               path.startsWith("/v3/api-docs/") ||
               path.startsWith("/swagger-resources/") ||
               path.equals("/api/test/public") ||
               path.equals("/favicon.ico");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String requestUri = request.getRequestURI();
            String method = request.getMethod();
            log.debug("Processing {} request to: {}", method, requestUri);
            
            // Log all headers for debugging
            log.debug("Request headers:");
            java.util.Enumeration<String> headerNames = request.getHeaderNames();
            while (headerNames.hasMoreElements()) {
                String headerName = headerNames.nextElement();
                if ("authorization".equalsIgnoreCase(headerName)) {
                    log.debug("  {}: [REDACTED FOR SECURITY]", headerName);
                } else {
                    log.debug("  {}: {}", headerName, request.getHeader(headerName));
                }
            }
            
            String jwt = getJwtFromRequest(request);

            if (StringUtils.hasText(jwt)) {
                log.debug("JWT token found in request");
                log.trace("JWT token: {}", jwt);

                try {
                    // 1. Validate the token
                    if (!tokenProvider.validateToken(jwt)) {
                        log.warn("Invalid JWT token - validation failed");
                        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid or expired token");
                        return;
                    }

                    // 2. Parse the JWT claims
                    Claims claims = Jwts.parserBuilder()
                            .setSigningKey(tokenProvider.getSigningKey())
                            .build()
                            .parseClaimsJws(jwt)
                            .getBody();

                    // 3. Extract user ID and roles
                    Long userId = Long.parseLong(claims.getSubject());

                    @SuppressWarnings("unchecked")
                    List<String> roles = claims.get("roles", List.class);
                    log.debug("Authenticating user ID: {}, roles: {}", userId, roles);

                    if (roles == null || roles.isEmpty()) {
                        log.warn("No roles found in JWT token for user ID: {}", userId);
                        response.sendError(HttpServletResponse.SC_FORBIDDEN, "No roles assigned");
                        return;
                    }

                    // 4. Convert roles to authorities with proper ROLE_ prefix
                    List<SimpleGrantedAuthority> authorities = roles.stream()
                            .map(role -> {
                                // Convert role to uppercase and ensure it has ROLE_ prefix
                                String roleName = role.toUpperCase();
                                if (!roleName.startsWith("ROLE_")) {
                                    roleName = "ROLE_" + roleName;
                                }
                                log.debug("Mapping role: {} to authority: {}", role, roleName);
                                return new SimpleGrantedAuthority(roleName);
                            })
                            .collect(Collectors.toList());
                            
                    log.debug("Converted roles to authorities: {}", authorities);

                    // 5. Load user details from database
                    try {
                        UserDetails userDetails = userDetailsService.loadUserById(userId);
                        log.debug("Successfully loaded user details for ID: {}", userId);
                        
                        // 6. Create authentication object with full user details
                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                                userDetails,   // principal with full user details
                                null,          // credentials (not needed after authentication)
                                authorities);

                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                        // 7. Set the authentication in the SecurityContext
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                        log.debug("Successfully authenticated user ID: {} with roles: {}", userId, roles);
                        
                    } catch (UsernameNotFoundException ex) {
                        log.warn("User not found in database for ID: {}", userId);
                        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "User not found");
                        return;
                    }

                } catch (ExpiredJwtException ex) {
                    log.warn("JWT token is expired: {}", ex.getMessage());
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token expired");
                    return;
                } catch (UnsupportedJwtException | MalformedJwtException ex) {
                    log.warn("Invalid JWT token: {}", ex.getMessage());
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
                    return;
                } catch (Exception ex) {
                    log.error("Authentication error", ex);
                    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Authentication error");
                    return;
                }
            } else {
                log.debug("No JWT token found in request for {} {}", request.getMethod(), request.getRequestURI());
                // Don't block the request here - let the controller handle authorization
            }

            // Continue the filter chain
            filterChain.doFilter(request, response);

        } catch (Exception ex) {
            log.error("Error in JWT authentication filter", ex);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Authentication error");
        }
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        log.debug("Authorization header: {}", bearerToken != null ? "[present]" : "[null]");
        
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            String token = bearerToken.substring(7);
            log.debug("Extracted JWT token (first 10 chars): {}", 
                token.length() > 10 ? token.substring(0, 10) + "..." : token);
            return token;
        } else if (StringUtils.hasText(bearerToken)) {
            log.warn("Authorization header does not start with 'Bearer ' prefix");
        } else {
            log.debug("No Authorization header found");
        }
        return null;
    }
}
