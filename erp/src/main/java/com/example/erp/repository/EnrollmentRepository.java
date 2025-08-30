package com.example.erp.repository;

import com.example.erp.entity.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    
    List<Enrollment> findByStudentId(Long studentId);
    
    List<Enrollment> findBySubjectId(Long subjectId);
    
    List<Enrollment> findByAcademicYear(String academicYear);
    
    List<Enrollment> findByStatus(Enrollment.EnrollmentStatus status);
    
    @Query("SELECT e FROM Enrollment e WHERE e.student.id = :studentId AND e.academicYear = :academicYear")
    List<Enrollment> findByStudentIdAndAcademicYear(@Param("studentId") Long studentId, @Param("academicYear") String academicYear);
    
    @Query("SELECT e FROM Enrollment e WHERE e.student.id = :studentId AND e.subject.id = :subjectId")
    List<Enrollment> findByStudentIdAndSubjectId(@Param("studentId") Long studentId, @Param("subjectId") Long subjectId);
    
    @Query("SELECT e FROM Enrollment e WHERE e.subject.department.id = :departmentId")
    List<Enrollment> findByDepartmentId(@Param("departmentId") Long departmentId);
}
