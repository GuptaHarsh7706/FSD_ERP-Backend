package com.example.erp.controller;

import com.example.erp.entity.Enrollment;
import com.example.erp.service.EnrollmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/enrollment")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class EnrollmentController {
    
    private final EnrollmentService enrollmentService;
    
    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRINCIPAL') or hasRole('STAFF')")
    public ResponseEntity<List<Enrollment>> getAllEnrollments() {
        List<Enrollment> enrollments = enrollmentService.getAllEnrollments();
        return ResponseEntity.ok(enrollments);
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRINCIPAL') or hasRole('STAFF') or hasRole('FACULTY')")
    public ResponseEntity<Enrollment> getEnrollmentById(@PathVariable Long id) {
        return enrollmentService.getEnrollmentById(id)
            .map(enrollment -> ResponseEntity.ok(enrollment))
            .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/student/{studentId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRINCIPAL') or hasRole('STAFF') or hasRole('FACULTY')")
    public ResponseEntity<List<Enrollment>> getEnrollmentsByStudent(@PathVariable Long studentId) {
        List<Enrollment> enrollments = enrollmentService.getEnrollmentsByStudent(studentId);
        return ResponseEntity.ok(enrollments);
    }
    
    @GetMapping("/subject/{subjectId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRINCIPAL') or hasRole('FACULTY')")
    public ResponseEntity<List<Enrollment>> getEnrollmentsBySubject(@PathVariable Long subjectId) {
        List<Enrollment> enrollments = enrollmentService.getEnrollmentsBySubject(subjectId);
        return ResponseEntity.ok(enrollments);
    }
    
    @PostMapping
    @PreAuthorize("hasRole('STAFF') or hasRole('PRINCIPAL')")
    public ResponseEntity<?> createEnrollment(@Valid @RequestBody Enrollment enrollment) {
        try {
            Enrollment createdEnrollment = enrollmentService.createEnrollment(enrollment);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdEnrollment);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                .body("Error creating enrollment: " + e.getMessage());
        }
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('STAFF') or hasRole('PRINCIPAL')")
    public ResponseEntity<?> updateEnrollment(@PathVariable Long id, @Valid @RequestBody Enrollment enrollment) {
        try {
            Enrollment updatedEnrollment = enrollmentService.updateEnrollment(id, enrollment);
            return ResponseEntity.ok(updatedEnrollment);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                .body("Error updating enrollment: " + e.getMessage());
        }
    }
    
    @PutMapping("/{id}/complete")
    @PreAuthorize("hasRole('FACULTY')")
    public ResponseEntity<?> completeEnrollment(@PathVariable Long id, @RequestParam String grade) {
        try {
            Enrollment completedEnrollment = enrollmentService.completeEnrollment(id, grade);
            return ResponseEntity.ok(completedEnrollment);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                .body("Error completing enrollment: " + e.getMessage());
        }
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('PRINCIPAL')")
    public ResponseEntity<?> deleteEnrollment(@PathVariable Long id) {
        try {
            enrollmentService.deleteEnrollment(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                .body("Error deleting enrollment: " + e.getMessage());
        }
    }
}
