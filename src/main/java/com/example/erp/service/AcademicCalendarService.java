package com.example.erp.service;

import com.example.erp.entity.AcademicCalendar;
import com.example.erp.repository.AcademicCalendarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class AcademicCalendarService {
    
    private final AcademicCalendarRepository academicCalendarRepository;
    
    public List<AcademicCalendar> getAllEvents() {
        return academicCalendarRepository.findAll();
    }
    
    public Optional<AcademicCalendar> getEventById(Long id) {
        return academicCalendarRepository.findById(id);
    }
    
    public List<AcademicCalendar> getEventsByAcademicYear(String academicYear) {
        return academicCalendarRepository.findByAcademicYear(academicYear);
    }
    
    public List<AcademicCalendar> getEventsByType(AcademicCalendar.EventType eventType) {
        return academicCalendarRepository.findByEventType(eventType);
    }
    
    public List<AcademicCalendar> getEventsByDepartment(Long departmentId) {
        return academicCalendarRepository.findByDepartmentId(departmentId);
    }
    
    public List<AcademicCalendar> getHolidays() {
        return academicCalendarRepository.findByIsHoliday(true);
    }
    
    public List<AcademicCalendar> getUpcomingEvents() {
        return academicCalendarRepository.findUpcomingEvents(LocalDate.now());
    }
    
    public List<AcademicCalendar> getEventsBetweenDates(LocalDate startDate, LocalDate endDate) {
        return academicCalendarRepository.findByEventDateBetween(startDate, endDate);
    }
    
    public AcademicCalendar createEvent(AcademicCalendar event) {
        event.setCreatedAt(LocalDateTime.now());
        return academicCalendarRepository.save(event);
    }
    
    public AcademicCalendar updateEvent(Long id, AcademicCalendar eventDetails) {
        AcademicCalendar event = academicCalendarRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Academic calendar event not found with id: " + id));
        
        if (eventDetails.getTitle() != null) {
            event.setTitle(eventDetails.getTitle());
        }
        if (eventDetails.getDescription() != null) {
            event.setDescription(eventDetails.getDescription());
        }
        if (eventDetails.getEventDate() != null) {
            event.setEventDate(eventDetails.getEventDate());
        }
        if (eventDetails.getEndDate() != null) {
            event.setEndDate(eventDetails.getEndDate());
        }
        if (eventDetails.getEventType() != null) {
            event.setEventType(eventDetails.getEventType());
        }
        if (eventDetails.getIsHoliday() != null) {
            event.setIsHoliday(eventDetails.getIsHoliday());
        }
        
        event.setUpdatedAt(LocalDateTime.now());
        return academicCalendarRepository.save(event);
    }
    
    public void deleteEvent(Long id) {
        if (!academicCalendarRepository.existsById(id)) {
            throw new RuntimeException("Academic calendar event not found with id: " + id);
        }
        academicCalendarRepository.deleteById(id);
    }
}
