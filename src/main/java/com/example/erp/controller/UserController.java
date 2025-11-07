package com.example.erp.controller;

import com.example.erp.entity.User;
import com.example.erp.service.UserService;
import jakarta.persistence.EntityNotFoundException;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class UserController {

    private final UserService userService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('FACULTY')")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('FACULTY') or @userService.getUserById(#id).get().userId == authentication.principal.userId")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return userService.getUserById(id)
                .map(user -> ResponseEntity.ok(user))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/email/{email}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('FACULTY')")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
        return userService.getUserByEmail(email)
                .map(user -> ResponseEntity.ok(user))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/role/{role}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('FACULTY')")
    public ResponseEntity<List<User>> getUsersByRole(@PathVariable User.UserRole role) {
        List<User> users = userService.getUsersByRole(role);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/department/{departmentId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('FACULTY')")
    public ResponseEntity<List<User>> getUsersByDepartment(@PathVariable Long departmentId) {
        List<User> users = userService.getUsersByDepartment(departmentId);
        return ResponseEntity.ok(users);
    }

    @PostMapping("/first-user")
    public ResponseEntity<?> registerFirstUser(@Valid @RequestBody User user) {
        // Check if any users exist
        if (userService.getUserCount() > 0) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Initial admin user already exists");
        }
        
        // Set first user as ADMIN
        user.setRole(User.UserRole.ADMIN);
        
        // Check for existing email
        if (userService.existsByEmail(user.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Email already in use");
        }
        
        User createdUser = userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }
    
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody User user) {
        try {
            log.info("Received registration request for user: {}", user.getEmail());
            
            // Check if database is empty (bootstrap case)
            long userCount = userService.getUserCount();
            log.debug("Current user count in database: {}", userCount);
            
            if (userCount == 0) {
                log.info("No users found in database. Creating first user as ADMIN");
                user.setRole(User.UserRole.ADMIN);
            } else {
                // For non-bootstrap case, check if user has ADMIN or PRINCIPAL role
                org.springframework.security.core.Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                log.debug("Authentication object: {}", auth);
                
                if (auth == null || !auth.isAuthenticated() || auth.getAuthorities() == null) {
                    log.warn("Unauthenticated or invalid authentication object");
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                            .body(Collections.singletonMap("error", "Authentication required to create new users"));
                }
                
                log.debug("User authorities: {}", auth.getAuthorities());
                
                boolean isAdmin = auth.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .map(role -> role.startsWith("ROLE_") ? role : "ROLE_" + role)
                        .peek(role -> log.debug("Checking role: {}", role))
                        .anyMatch(role -> role.equals("ROLE_ADMIN") || role.equals("ROLE_PRINCIPAL"));
                
                log.info("User has admin/principal role: {}", isAdmin);
                                
                if (!isAdmin) {
                    log.warn("Access denied - user does not have required admin/principal role. Roles: {}", auth.getAuthorities());
                    return ResponseEntity.status(HttpStatus.FORBIDDEN)
                            .body(Collections.singletonMap("error", 
                                "Only administrators (ADMIN/PRINCIPAL) can create new users. Current roles: " + auth.getAuthorities()));
                }
                
                // Get current user's role
                String currentUserEmail = auth.getName();
                Optional<User> currentUser = userService.getUserByEmail(currentUserEmail);
                
                if (!currentUser.isPresent()) {
                    log.error("Authenticated user not found in database: {}", currentUserEmail);
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body(Collections.singletonMap("error", "User not found in database"));
                }
                
                User.UserRole currentRole = currentUser.get().getRole();
                User.UserRole targetRole = user.getRole() != null ? user.getRole() : User.UserRole.STUDENT;
                
                // Role-based access control
                if (currentRole == User.UserRole.PRINCIPAL) {
                    // PRINCIPAL can only create FACULTY, STAFF, STUDENT
                    if (targetRole == User.UserRole.ADMIN || targetRole == User.UserRole.PRINCIPAL) {
                        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                                .body(Collections.singletonMap("error", 
                                    "PRINCIPAL can only create FACULTY, STAFF, or STUDENT users"));
                    }
                } else if (currentRole == User.UserRole.ADMIN) {
                    // ADMIN can create any role
                    if (targetRole == null) {
                        user.setRole(User.UserRole.STAFF); // Default for admin-created users
                    }
                } else {
                    // Only ADMIN and PRINCIPAL can create users
                    return ResponseEntity.status(HttpStatus.FORBIDDEN)
                            .body(Collections.singletonMap("error", "Insufficient privileges to create users"));
                }
                
                // Ensure the role is set
                if (user.getRole() == null) {
                    user.setRole(User.UserRole.STUDENT);
                }
            }
            
            // Check for existing email before attempting to create user
            if (userService.existsByEmail(user.getEmail())) {
                log.warn("Registration failed - email already in use: {}", user.getEmail());
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(Collections.singletonMap("error", "Email already in use"));
            }
            
            log.info("Creating new user with email: {}, role: {}", user.getEmail(), user.getRole());
            User createdUser = userService.createUser(user);
            log.info("Successfully created user with ID: {}", createdUser.getUserId());
            
            // Return a clean response with only necessary user details
            Map<String, Object> response = new HashMap<>();
            response.put("userId", createdUser.getUserId());
            response.put("email", createdUser.getEmail());
            response.put("name", createdUser.getName());
            response.put("role", createdUser.getRole());
            response.put("message", "User registered successfully");
            
            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (Exception e) {
            log.error("Error in registerUser: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", "An error occurred while processing your request: " + e.getMessage()));
        }
    }

    @GetMapping("/count")
    public ResponseEntity<Long> getUserCount() {
        return ResponseEntity.ok(userService.getUserCount());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createUser(@Valid @RequestBody User user) {
        try {
            User createdUser = userService.createUser(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body("Error creating user: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRINCIPAL') or @userService.getUserById(#id).get().email == authentication.name")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @Valid @RequestBody User userDetails) {
        try {
            // Get current user
            String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
            Optional<User> currentUser = userService.getUserByEmail(currentUserEmail);
            Optional<User> targetUser = userService.getUserById(id);

            if (!currentUser.isPresent() || !targetUser.isPresent()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            User.UserRole currentRole = currentUser.get().getRole();
            User.UserRole targetRole = targetUser.get().getRole();

            // Check permissions for updating other users
            if (!currentUser.get().getUserId().equals(targetUser.get().getUserId())) {
                // ADMIN can update anyone
                // PRINCIPAL can update FACULTY, STAFF, STUDENT
                if (currentRole == User.UserRole.PRINCIPAL) {
                    if (targetRole == User.UserRole.ADMIN || targetRole == User.UserRole.PRINCIPAL) {
                        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                                .body("PRINCIPAL cannot update ADMIN or PRINCIPAL users");
                    }
                } else if (currentRole != User.UserRole.ADMIN) {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN)
                            .body("You can only update your own profile");
                }
            }

            // Prevent role changes unless authorized
            if (userDetails.getRole() != null && !userDetails.getRole().equals(targetRole)) {
                if (currentRole == User.UserRole.PRINCIPAL) {
                    if (userDetails.getRole() == User.UserRole.ADMIN
                            || userDetails.getRole() == User.UserRole.PRINCIPAL) {
                        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                                .body("PRINCIPAL cannot assign ADMIN or PRINCIPAL roles");
                    }
                } else if (currentRole != User.UserRole.ADMIN) {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN)
                            .body("You cannot change user roles");
                }
            }

            User updatedUser = userService.updateUser(id, userDetails);
            return ResponseEntity.ok(updatedUser);

        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating user: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        try {
            // Get current user
            String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
            Optional<User> currentUser = userService.getUserByEmail(currentUserEmail);
            
            if (currentUser.isPresent() && currentUser.get().getUserId().equals(id)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(Collections.singletonMap("error", "Cannot delete your own account"));
            }
            
            userService.deleteUser(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(Collections.singletonMap("error", e.getMessage()));
        }
    }

    @GetMapping("/search")
    @PreAuthorize("hasRole('ADMIN') or hasRole('FACULTY')")
    public ResponseEntity<List<User>> searchUsers(@RequestParam String name) {
        List<User> users = userService.searchUsersByName(name);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/count/role/{role}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Long> getUserCountByRole(@PathVariable User.UserRole role) {
        long count = userService.getUserCountByRole(role);
        return ResponseEntity.ok(count);
    }

}
