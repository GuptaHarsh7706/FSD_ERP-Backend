package com.example.erp.repository;

import com.example.erp.entity.GradeCalculation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GradeCalculationRepository extends JpaRepository<GradeCalculation, Long> {
    
    List<GradeCalculation> findByStudentId(Long studentId);
    
    List<GradeCalculation> findBySubjectId(Long subjectId);
    
    List<GradeCalculation> findByAcademicYear(String academicYear);
    
    List<GradeCalculation> findByStatus(GradeCalculation.GradeStatus status);
    
    @Query("SELECT gc FROM GradeCalculation gc WHERE gc.studentId = :studentId AND gc.academicYear = :academicYear")
    List<GradeCalculation> findByStudentIdAndAcademicYear(@Param("studentId") Long studentId, @Param("academicYear") String academicYear);
    
    @Query("SELECT gc FROM GradeCalculation gc WHERE gc.studentId = :studentId AND gc.subjectId = :subjectId AND gc.academicYear = :academicYear")
    Optional<GradeCalculation> findByStudentIdAndSubjectIdAndAcademicYear(
        @Param("studentId") Long studentId, 
        @Param("subjectId") Long subjectId, 
        @Param("academicYear") String academicYear
    );
    
    @Query("SELECT AVG(gc.gradePoints) FROM GradeCalculation gc WHERE gc.studentId = :studentId AND gc.academicYear = :academicYear AND gc.status = :status")
    Double calculateGPA(@Param("studentId") Long studentId, @Param("academicYear") String academicYear, @Param("status") GradeCalculation.GradeStatus status);
    
    @Query("SELECT gc FROM GradeCalculation gc WHERE gc.academicYear = :academicYear AND gc.semester = :semester")
    List<GradeCalculation> findByAcademicYearAndSemester(@Param("academicYear") String academicYear, @Param("semester") Integer semester);
}
