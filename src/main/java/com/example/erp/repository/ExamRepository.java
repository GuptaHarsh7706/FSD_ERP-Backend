package com.example.erp.repository;

import com.example.erp.entity.Exam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ExamRepository extends JpaRepository<Exam, Long> {
    
    List<Exam> findBySubjectId(Long subjectId);
    
    List<Exam> findByDepartmentId(Long departmentId);
    
    List<Exam> findByDate(LocalDate date);
    
    List<Exam> findByDateBetween(LocalDate startDate, LocalDate endDate);
    
    @Query("SELECT e FROM Exam e WHERE e.subjectId = :subjectId AND e.date BETWEEN :startDate AND :endDate")
    List<Exam> findBySubjectIdAndDateRange(@Param("subjectId") Long subjectId, 
                                          @Param("startDate") LocalDate startDate, 
                                          @Param("endDate") LocalDate endDate);
    
    @Query("SELECT e FROM Exam e WHERE e.name LIKE CONCAT('%', :name, '%')")
    List<Exam> findByNameContaining(@Param("name") String name);
}
