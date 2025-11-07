package com.example.erp.repository;

import com.example.erp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    Optional<User> findByEmail(String email);
    
    List<User> findByRole(User.UserRole role);
    
    List<User> findByDepartmentId(Long departmentId);
    
    @Query("SELECT u FROM User u WHERE u.name LIKE CONCAT('%', :name, '%')")
    List<User> findByNameContaining(@Param("name") String name);
    
    boolean existsByEmail(String email);
    
    @Query("SELECT COUNT(u) FROM User u WHERE u.role = :role")
    long countByRole(@Param("role") User.UserRole role);
}
