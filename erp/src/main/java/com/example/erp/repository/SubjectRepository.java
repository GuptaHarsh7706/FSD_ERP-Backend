package com.example.erp.repository;

import com.example.erp.entity.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {
    
    Optional<Subject> findByCode(String code);
    
    List<Subject> findByDepartmentId(Long departmentId);
    
    List<Subject> findBySemester(Integer semester);
    
    List<Subject> findByDepartmentIdAndSemester(Long departmentId, Integer semester);
    
    @Query("SELECT s FROM Subject s WHERE s.name LIKE CONCAT('%', :name, '%')")
    List<Subject> findByNameContaining(@Param("name") String name);
    
    boolean existsByCode(String code);
}
