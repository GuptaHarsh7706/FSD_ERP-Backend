package com.example.erp.controller;

import com.example.erp.entity.AcademicCalendar;
import com.example.erp.service.AcademicCalendarService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/academic-calendar")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AcademicCalendarController {
    
    private final AcademicCalendarService academicCalendarService;
    
    @GetMapping
    public ResponseEntity<List<AcademicCalendar>> getAllEvents() {
        List<AcademicCalendar> events = academicCalendarService.getAllEvents();
        return ResponseEntity.ok(events);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<AcademicCalendar> getEventById(@PathVariable Long id) {
        return academicCalendarService.getEventById(id)
            .map(event -> ResponseEntity.ok(event))
            .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/academic-year/{academicYear}")
    public ResponseEntity<List<AcademicCalendar>> getEventsByAcademicYear(@PathVariable String academicYear) {
        List<AcademicCalendar> events = academicCalendarService.getEventsByAcademicYear(academicYear);
        return ResponseEntity.ok(events);
    }
    
    @GetMapping("/upcoming")
    public ResponseEntity<List<AcademicCalendar>> getUpcomingEvents() {
        List<AcademicCalendar> events = academicCalendarService.getUpcomingEvents();
        return ResponseEntity.ok(events);
    }
    
    @GetMapping("/holidays")
    public ResponseEntity<List<AcademicCalendar>> getHolidays() {
        List<AcademicCalendar> holidays = academicCalendarService.getHolidays();
        return ResponseEntity.ok(holidays);
    }
    
    @GetMapping("/date-range")
    public ResponseEntity<List<AcademicCalendar>> getEventsBetweenDates(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<AcademicCalendar> events = academicCalendarService.getEventsBetweenDates(startDate, endDate);
        return ResponseEntity.ok(events);
    }
    
    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRINCIPAL')")
    public ResponseEntity<?> createEvent(@Valid @RequestBody AcademicCalendar event) {
        try {
            AcademicCalendar createdEvent = academicCalendarService.createEvent(event);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdEvent);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                .body("Error creating academic calendar event: " + e.getMessage());
        }
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRINCIPAL')")
    public ResponseEntity<?> updateEvent(@PathVariable Long id, @Valid @RequestBody AcademicCalendar event) {
        try {
            AcademicCalendar updatedEvent = academicCalendarService.updateEvent(id, event);
            return ResponseEntity.ok(updatedEvent);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                .body("Error updating academic calendar event: " + e.getMessage());
        }
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteEvent(@PathVariable Long id) {
        try {
            academicCalendarService.deleteEvent(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                .body("Error deleting academic calendar event: " + e.getMessage());
        }
    }
}
