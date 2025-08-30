package com.example.erp.controller;

import com.example.erp.entity.User;
import com.example.erp.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
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

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody User user) {
        try {
            // Check if database is empty (bootstrap case)
            if (userService.getUserCount() == 0) {
                // Force first user to be ADMIN for bootstrap
                user.setRole(User.UserRole.ADMIN);
                
                // Check for existing email before attempting to create user
                if (userService.existsByEmail(user.getEmail())) {
                    return ResponseEntity.status(HttpStatus.CONFLICT)
                            .body("Email already in use");
                }
                
                User createdUser = userService.createUser(user);
                return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
            }

            // Normal user creation flow (when users exist)
            // Check if user is authenticated for normal operations
            if (SecurityContextHolder.getContext().getAuthentication() == null || 
                SecurityContextHolder.getContext().getAuthentication().getName().equals("anonymousUser")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Authentication required when users exist in the system");
            }

            // Get current user to check permissions
            String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
            Optional<User> currentUser = userService.getUserByEmail(currentUserEmail);

            if (!currentUser.isPresent()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            // Check role-based permissions
            User.UserRole currentRole = currentUser.get().getRole();
            if (currentRole != User.UserRole.ADMIN && currentRole != User.UserRole.PRINCIPAL) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Only ADMIN or PRINCIPAL can create users");
            }

            // Default to STUDENT if no role is provided
            if (user.getRole() == null) {
                user.setRole(User.UserRole.STUDENT);
            }

            // Only PRINCIPAL can create FACULTY, STAFF, STUDENT users
            // Only ADMIN can create PRINCIPAL users
            User.UserRole targetRole = user.getRole();

            if (currentRole == User.UserRole.PRINCIPAL) {
                if (targetRole == User.UserRole.ADMIN || targetRole == User.UserRole.PRINCIPAL) {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN)
                            .body("PRINCIPAL cannot create ADMIN or PRINCIPAL users");
                }
            }

            // Check for existing email before attempting to create user
            if (userService.existsByEmail(user.getEmail())) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("Email already in use");
            }

            User createdUser = userService.createUser(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body("Error creating user: " + e.getMessage());
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
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
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
