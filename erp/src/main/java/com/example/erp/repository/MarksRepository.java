package com.example.erp.repository;

import com.example.erp.entity.Marks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MarksRepository extends JpaRepository<Marks, Long> {
    
    List<Marks> findByStudentId(Long studentId);
    
    List<Marks> findByExamId(Long examId);
    
    List<Marks> findByStudentIdAndExamId(Long studentId, Long examId);
    
    @Query("SELECT m FROM Marks m WHERE m.exam.subjectId = :subjectId")
    List<Marks> findBySubjectId(@Param("subjectId") Long subjectId);
    
    @Query("SELECT m FROM Marks m WHERE m.studentId = :studentId AND m.exam.subjectId = :subjectId")
    List<Marks> findByStudentIdAndSubjectId(@Param("studentId") Long studentId, @Param("subjectId") Long subjectId);
    
    @Query("SELECT AVG(m.marksObtained) FROM Marks m WHERE m.studentId = :studentId")
    Double findAverageMarksByStudentId(@Param("studentId") Long studentId);
}
