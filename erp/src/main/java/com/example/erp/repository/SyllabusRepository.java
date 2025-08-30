package com.example.erp.repository;

import com.example.erp.entity.Syllabus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SyllabusRepository extends JpaRepository<Syllabus, Long> {
    
    List<Syllabus> findBySubjectId(Long subjectId);
    
    List<Syllabus> findByDepartmentId(Long departmentId);
    
    List<Syllabus> findByAcademicYear(String academicYear);
    
    List<Syllabus> findByStatus(String status);
}
