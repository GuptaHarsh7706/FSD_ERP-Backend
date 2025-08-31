package com.example.erp.security;

import com.example.erp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final Environment env;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public RoleHierarchy roleHierarchy() {
        String hierarchy = "ROLE_ADMIN > ROLE_PRINCIPAL > ROLE_FACULTY > ROLE_STAFF > ROLE_STUDENT";
        return RoleHierarchyImpl.fromHierarchy(hierarchy);
    }

    @Bean
    public DefaultWebSecurityExpressionHandler customWebSecurityExpressionHandler() {
        DefaultWebSecurityExpressionHandler expressionHandler = new DefaultWebSecurityExpressionHandler();
        expressionHandler.setRoleHierarchy(roleHierarchy());
        return expressionHandler;
    }

    @Bean
    @Order(1)
    public SecurityFilterChain securityFilterChain(HttpSecurity http, UserService userService) throws Exception {
        // Public endpoints (no authentication required)
        String[] SWAGGER_WHITELIST = {
                // Swagger UI v3 (OpenAPI)
                "/v3/api-docs/**",
                "/v3/api-docs",
                "/swagger-ui/**",
                "/swagger-ui.html",
                "/swagger-resources/**",
                "/webjars/**"
        };

        String[] PUBLIC_ENDPOINTS = {
                // Public API endpoints
                "/api/auth/**",
                "/api/test/public",
                
                // Allow first user registration
                "/api/users/first-user",

                // Actuator endpoints
                "/actuator/health",

                // H2 Console (for development)
                "/h2-console/**"
        };

        // Configure security
        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> {
                // Allow all public endpoints
                for (String pattern : SWAGGER_WHITELIST) {
                    auth.requestMatchers(pattern).permitAll();
                }
                for (String pattern : PUBLIC_ENDPOINTS) {
                    auth.requestMatchers(pattern).permitAll();
                }
                
                        // Explicitly allow registration endpoint but still process JWT if present
                // The controller will handle role-based access control
                auth.requestMatchers("/api/users/register").permitAll();
                
                // All other API endpoints require authentication
                auth.requestMatchers("/api/**").authenticated();
                
                // Secure all other endpoints
                auth.anyRequest().authenticated();
            })
            .userDetailsService(customUserDetailsService)
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
            .exceptionHandling(exception -> exception
                .authenticationEntryPoint((request, response, authException) -> {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized: " + authException.getMessage());
                })
                .accessDeniedHandler((request, response, accessDeniedException) -> {
                    response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied: " + accessDeniedException.getMessage());
                }));

        // For H2 Console
        http.headers(headers -> headers.frameOptions(frameOptions -> frameOptions.sameOrigin()));

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        
        // Allow all origins, methods, and headers for testing
        configuration.setAllowedOrigins(List.of("*"));
        configuration.setAllowedMethods(List.of("*"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setExposedHeaders(List.of("Authorization", "Content-Disposition"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);
        
        // For production, you can add specific origins
        if (!env.acceptsProfiles(profiles -> profiles.test("dev") || profiles.test("local"))) {
            // Production CORS settings
            configuration.setAllowedOrigins(List.of(
                    "https://your-production-domain.com",
                    "https://www.your-production-domain.com"));
            configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
            configuration.setAllowedHeaders(List.of(
                    "Authorization", "Content-Type", "X-Requested-With", "Accept", "Origin",
                    "Access-Control-Request-Method", "Access-Control-Request-Headers"));
            configuration.setExposedHeaders(List.of("Authorization", "Content-Disposition"));
            configuration.setAllowCredentials(true);
            configuration.setMaxAge(3600L);
        }

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
