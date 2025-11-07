package com.example.erp.security;

import com.example.erp.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@Slf4j
public class UserPrincipal implements UserDetails {
    
    private Long userId;
    private String email;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;
    
    public static UserPrincipal create(User user) {
        log.debug("Creating UserPrincipal for user: {} (ID: {})", user.getEmail(), user.getUserId());
        
        // Convert user roles to authorities with ROLE_ prefix
        String roleName = "ROLE_" + user.getRole().name();
        log.debug("User role: {}", roleName);
        
        Collection<GrantedAuthority> authorities = Collections.singletonList(
            new SimpleGrantedAuthority(roleName)
        );
        
        // Log all authorities for debugging
        String authList = authorities.stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.joining(", "));
        log.debug("User authorities: {}", authList);
        
        return new UserPrincipal(
            user.getUserId(),
            user.getEmail(),
            user.getPassword(),
            authorities
        );
    }
    
    @Override
    public String getUsername() {
        return email;
    }
    
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    
    @Override
    public boolean isEnabled() {
        return true;
    }
}
