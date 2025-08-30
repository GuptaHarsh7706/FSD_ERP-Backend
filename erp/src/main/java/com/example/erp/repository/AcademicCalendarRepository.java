package com.example.erp.repository;

import com.example.erp.entity.AcademicCalendar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AcademicCalendarRepository extends JpaRepository<AcademicCalendar, Long> {
    
    List<AcademicCalendar> findByAcademicYear(String academicYear);
    
    List<AcademicCalendar> findByEventType(AcademicCalendar.EventType eventType);
    
    List<AcademicCalendar> findByDepartmentId(Long departmentId);
    
    List<AcademicCalendar> findByIsHoliday(Boolean isHoliday);
    
    @Query("SELECT ac FROM AcademicCalendar ac WHERE ac.eventDate BETWEEN :startDate AND :endDate")
    List<AcademicCalendar> findByEventDateBetween(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
    
    @Query("SELECT ac FROM AcademicCalendar ac WHERE ac.academicYear = :academicYear AND ac.semester = :semester")
    List<AcademicCalendar> findByAcademicYearAndSemester(@Param("academicYear") String academicYear, @Param("semester") Integer semester);
    
    @Query("SELECT ac FROM AcademicCalendar ac WHERE ac.eventDate >= :currentDate")
    List<AcademicCalendar> findUpcomingEvents(@Param("currentDate") LocalDate currentDate);
}
