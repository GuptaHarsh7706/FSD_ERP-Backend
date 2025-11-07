package com.example.erp.repository;

import com.example.erp.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    
    Optional<Student> findByPrnNumber(String prnNumber);
    
    Optional<Student> findByUserId(Long userId);
    
    List<Student> findByBatch(String batch);
    
    List<Student> findBySemester(Integer semester);
    
    List<Student> findByBatchAndSemester(String batch, Integer semester);
    
    @Query("SELECT s FROM Student s WHERE s.user.departmentId = :departmentId")
    List<Student> findByDepartmentId(@Param("departmentId") Long departmentId);
    
    boolean existsByPrnNumber(String prnNumber);
    
    @Query("SELECT COUNT(s) FROM Student s WHERE s.semester = :semester")
    long countBySemester(@Param("semester") Integer semester);
}
