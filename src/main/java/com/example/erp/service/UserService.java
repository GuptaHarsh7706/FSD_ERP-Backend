package com.example.erp.service;

import com.example.erp.entity.User;
import com.example.erp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final LogService logService;
    
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }
    
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    
    public List<User> getUsersByRole(User.UserRole role) {
        return userRepository.findByRole(role);
    }
    
    public List<User> getUsersByDepartment(Long departmentId) {
        return userRepository.findByDepartmentId(departmentId);
    }
    
    @Transactional
    public User createUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("User with email " + user.getEmail() + " already exists");
        }
        
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        
        User savedUser = userRepository.save(user);
        userRepository.flush(); // Force immediate database write
        
        try {
            logService.logAction(null, "CREATE", "User", savedUser.getUserId());
        } catch (Exception e) {
            System.err.println("Failed to log user creation: " + e.getMessage());
        }
        
        return savedUser;
    }
    
    public User updateUser(Long id, User userDetails) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        
        if (userDetails.getName() != null && !userDetails.getName().trim().isEmpty()) {
            user.setName(userDetails.getName());
        }
        
        if (userDetails.getEmail() != null && !user.getEmail().equals(userDetails.getEmail())) {
            if (userRepository.existsByEmail(userDetails.getEmail())) {
                throw new RuntimeException("Email already in use by another user");
            }
            user.setEmail(userDetails.getEmail());
        }
        
        if (userDetails.getPhoneNumber() != null) {
            user.setPhoneNumber(userDetails.getPhoneNumber());
        }
        
        if (userDetails.getRole() != null) {
            user.setRole(userDetails.getRole());
        }
        
        if (userDetails.getDepartmentId() != null) {
            user.setDepartmentId(userDetails.getDepartmentId());
        }
        
        if (userDetails.getPassword() != null && !userDetails.getPassword().trim().isEmpty()) {
            if (userDetails.getPassword().length() < 8) {
                throw new RuntimeException("Password must be at least 8 characters long");
            }
            user.setPassword(passwordEncoder.encode(userDetails.getPassword()));
        }
        
        User updatedUser = userRepository.save(user);
        logService.logAction(id, "UPDATE", "User", id);
        return updatedUser;
    }
    
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        
        if (user.getRole() == User.UserRole.ADMIN) {
            long adminCount = userRepository.countByRole(User.UserRole.ADMIN);
            if (adminCount <= 1) {
                throw new RuntimeException("Cannot delete the last ADMIN user. System must have at least one admin.");
            }
        }
        
        if (user.getStudent() != null) {
            throw new RuntimeException("Cannot delete user with associated student record. Delete student record first.");
        }
        
        if (user.getFaculty() != null) {
            throw new RuntimeException("Cannot delete user with associated faculty record. Delete faculty record first.");
        }
        
        userRepository.delete(user);
        logService.logAction(id, "DELETE", "User", id);
    }
    
    public List<User> searchUsersByName(String name) {
        return userRepository.findByNameContaining(name);
    }
    
    public long getUserCountByRole(User.UserRole role) {
        return userRepository.countByRole(role);
    }
    
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public long getUserCount() {
        return userRepository.count();
    }
}
