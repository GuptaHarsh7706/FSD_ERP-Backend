package com.example.erp.controller;

import com.example.erp.entity.Timetable;
import com.example.erp.service.TimetableService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/timetable")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class TimetableController {
    
    private final TimetableService timetableService;
    
    @GetMapping
    public ResponseEntity<List<Timetable>> getAllTimetables() {
        List<Timetable> timetables = timetableService.getAllTimetables();
        return ResponseEntity.ok(timetables);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Timetable> getTimetableById(@PathVariable Long id) {
        return timetableService.getTimetableById(id)
            .map(timetable -> ResponseEntity.ok(timetable))
            .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/department/{departmentId}")
    public ResponseEntity<List<Timetable>> getTimetableByDepartment(@PathVariable Long departmentId) {
        List<Timetable> timetables = timetableService.getTimetableByDepartment(departmentId);
        return ResponseEntity.ok(timetables);
    }
    
    @GetMapping("/subject/{subjectId}")
    public ResponseEntity<List<Timetable>> getTimetableBySubject(@PathVariable Long subjectId) {
        List<Timetable> timetables = timetableService.getTimetableBySubject(subjectId);
        return ResponseEntity.ok(timetables);
    }
    
    @GetMapping("/faculty/{facultyId}")
    public ResponseEntity<List<Timetable>> getTimetableByFaculty(@PathVariable Long facultyId) {
        List<Timetable> timetables = timetableService.getTimetableByFaculty(facultyId);
        return ResponseEntity.ok(timetables);
    }
    
    @PostMapping
    @PreAuthorize("hasRole('FACULTY')")
    public ResponseEntity<?> createTimetable(@Valid @RequestBody Timetable timetable) {
        try {
            Timetable createdTimetable = timetableService.createTimetable(timetable);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdTimetable);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                .body("Error creating timetable: " + e.getMessage());
        }
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('FACULTY')")
    public ResponseEntity<?> updateTimetable(@PathVariable Long id, @Valid @RequestBody Timetable timetable) {
        try {
            Timetable updatedTimetable = timetableService.updateTimetable(id, timetable);
            return ResponseEntity.ok(updatedTimetable);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                .body("Error updating timetable: " + e.getMessage());
        }
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('FACULTY')")
    public ResponseEntity<?> deleteTimetable(@PathVariable Long id) {
        try {
            timetableService.deleteTimetable(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                .body("Error deleting timetable: " + e.getMessage());
        }
    }
}
