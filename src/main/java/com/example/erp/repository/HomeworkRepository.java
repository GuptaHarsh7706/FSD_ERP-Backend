package com.example.erp.repository;

import com.example.erp.entity.Homework;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface HomeworkRepository extends JpaRepository<Homework, Long> {
    
    List<Homework> findBySubjectId(Long subjectId);
    
    List<Homework> findByFacultyId(Long facultyId);
    
    List<Homework> findByStudentId(Long studentId);
    
    List<Homework> findByDueDate(LocalDate dueDate);
    
    List<Homework> findByDueDateBetween(LocalDate startDate, LocalDate endDate);
    
    @Query("SELECT h FROM Homework h WHERE h.subjectId = :subjectId AND h.dueDate >= :currentDate")
    List<Homework> findUpcomingHomeworkBySubject(@Param("subjectId") Long subjectId, 
                                                @Param("currentDate") LocalDate currentDate);
    
    @Query("SELECT h FROM Homework h WHERE h.facultyId = :facultyId AND h.dueDate >= :currentDate")
    List<Homework> findUpcomingHomeworkByFaculty(@Param("facultyId") Long facultyId, 
                                                @Param("currentDate") LocalDate currentDate);
}
