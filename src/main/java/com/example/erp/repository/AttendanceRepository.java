package com.example.erp.repository;

import com.example.erp.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    
    List<Attendance> findByStudentId(Long studentId);
    
    List<Attendance> findBySubjectId(Long subjectId);
    
    List<Attendance> findByDate(LocalDate date);
    
    List<Attendance> findByStudentIdAndSubjectId(Long studentId, Long subjectId);
    
    List<Attendance> findByStudentIdAndDateBetween(Long studentId, LocalDate startDate, LocalDate endDate);
    
    @Query("SELECT a FROM Attendance a WHERE a.studentId = :studentId AND a.subjectId = :subjectId AND a.date BETWEEN :startDate AND :endDate")
    List<Attendance> findByStudentSubjectAndDateRange(@Param("studentId") Long studentId, 
                                                      @Param("subjectId") Long subjectId,
                                                      @Param("startDate") LocalDate startDate, 
                                                      @Param("endDate") LocalDate endDate);
    
    @Query("SELECT COUNT(a) FROM Attendance a WHERE a.studentId = :studentId AND a.subjectId = :subjectId AND a.status = :status")
    long countPresentByStudentAndSubject(@Param("studentId") Long studentId, @Param("subjectId") Long subjectId, @Param("status") Attendance.AttendanceStatus status);
}
