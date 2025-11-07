package com.example.erp.repository;

import com.example.erp.entity.Timetable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TimetableRepository extends JpaRepository<Timetable, Long> {
    
    List<Timetable> findBySubjectId(Long subjectId);
    
    List<Timetable> findByFacultyId(Long facultyId);
    
    List<Timetable> findByDepartmentId(Long departmentId);
    
    List<Timetable> findBySemester(Integer semester);
    
    List<Timetable> findByDayOfWeek(String dayOfWeek);
    
    @Query("SELECT t FROM Timetable t WHERE t.facultyId = :facultyId AND t.dayOfWeek = :dayOfWeek")
    List<Timetable> findByFacultyIdAndDayOfWeek(@Param("facultyId") Long facultyId, 
                                               @Param("dayOfWeek") String dayOfWeek);
    
    @Query("SELECT t FROM Timetable t WHERE t.departmentId = :departmentId AND t.semester = :semester")
    List<Timetable> findByDepartmentIdAndSemester(@Param("departmentId") Long departmentId, 
                                                  @Param("semester") Integer semester);
}
