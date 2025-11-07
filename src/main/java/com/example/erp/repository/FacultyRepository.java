package com.example.erp.repository;

import com.example.erp.entity.Faculty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FacultyRepository extends JpaRepository<Faculty, Long> {
    
    Optional<Faculty> findByUserId(Long userId);
    
    List<Faculty> findByDesignation(String designation);
    
    @Query("SELECT f FROM Faculty f WHERE f.user.departmentId = :departmentId")
    List<Faculty> findByDepartmentId(@Param("departmentId") Long departmentId);
    
    @Query("SELECT f FROM Faculty f WHERE f.designation LIKE CONCAT('%', :designation, '%')")
    List<Faculty> findByDesignationContaining(@Param("designation") String designation);
}
