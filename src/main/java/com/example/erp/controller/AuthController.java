package com.example.erp.controller;

import com.example.erp.dto.LoginRequest;
import com.example.erp.dto.LoginResponse;
import com.example.erp.security.JwtTokenProvider;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for handling authentication requests
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuthController {
    
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    
    /**
     * Authenticate user and return JWT token
     * @param loginRequest The login request containing email and password
     * @return JWT token and user details if authentication is successful
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.getEmail(),
                    loginRequest.getPassword()
                )
            );
            
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String token = tokenProvider.generateToken(authentication);
            
            return ResponseEntity.ok(new LoginResponse(
                token, 
                "Bearer", 
                authentication.getName(),
                authentication.getAuthorities().stream()
                    .findFirst()
                    .map(Object::toString)
                    .orElse("ROLE_USER")
            ));
            
        } catch (DisabledException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new LoginResponse("User account is disabled"));
                
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new LoginResponse("Invalid email or password"));
                
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new LoginResponse("Authentication failed: " + e.getMessage()));
        }
    }
    
    /**
     * Logout user (client should remove the token)
     * @return Success message
     */
    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        // In a stateless JWT system, logout is handled client-side by removing the token
        // For server-side token invalidation, you'd need a token blacklist or similar mechanism
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok("Logged out successfully. Please remove the token from client storage.");
    }
}
