package com.example.erp.controller;

import com.example.erp.entity.GradeCalculation;
import com.example.erp.service.GradeCalculationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/grade-calculation")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class GradeCalculationController {
    
    private final GradeCalculationService gradeCalculationService;
    
    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRINCIPAL') or hasRole('FACULTY')")
    public ResponseEntity<List<GradeCalculation>> getAllGradeCalculations() {
        List<GradeCalculation> gradeCalculations = gradeCalculationService.getAllGradeCalculations();
        return ResponseEntity.ok(gradeCalculations);
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRINCIPAL') or hasRole('FACULTY')")
    public ResponseEntity<GradeCalculation> getGradeCalculationById(@PathVariable Long id) {
        return gradeCalculationService.getGradeCalculationById(id)
            .map(gradeCalculation -> ResponseEntity.ok(gradeCalculation))
            .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/student/{studentId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRINCIPAL') or hasRole('FACULTY') or hasRole('STAFF')")
    public ResponseEntity<List<GradeCalculation>> getGradeCalculationsByStudent(@PathVariable Long studentId) {
        List<GradeCalculation> gradeCalculations = gradeCalculationService.getGradeCalculationsByStudent(studentId);
        return ResponseEntity.ok(gradeCalculations);
    }
    
    @GetMapping("/subject/{subjectId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRINCIPAL') or hasRole('FACULTY')")
    public ResponseEntity<List<GradeCalculation>> getGradeCalculationsBySubject(@PathVariable Long subjectId) {
        List<GradeCalculation> gradeCalculations = gradeCalculationService.getGradeCalculationsBySubject(subjectId);
        return ResponseEntity.ok(gradeCalculations);
    }
    
    @GetMapping("/gpa/{studentId}/{academicYear}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRINCIPAL') or hasRole('FACULTY') or hasRole('STAFF')")
    public ResponseEntity<Double> calculateGPA(@PathVariable Long studentId, @PathVariable String academicYear) {
        Double gpa = gradeCalculationService.calculateGPA(studentId, academicYear);
        return ResponseEntity.ok(gpa != null ? gpa : 0.0);
    }
    
    @PostMapping
    @PreAuthorize("hasRole('FACULTY')")
    public ResponseEntity<?> createGradeCalculation(@Valid @RequestBody GradeCalculation gradeCalculation) {
        try {
            GradeCalculation createdGradeCalculation = gradeCalculationService.createGradeCalculation(gradeCalculation);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdGradeCalculation);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                .body("Error creating grade calculation: " + e.getMessage());
        }
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('FACULTY')")
    public ResponseEntity<?> updateGradeCalculation(@PathVariable Long id, @Valid @RequestBody GradeCalculation gradeCalculation) {
        try {
            GradeCalculation updatedGradeCalculation = gradeCalculationService.updateGradeCalculation(id, gradeCalculation);
            return ResponseEntity.ok(updatedGradeCalculation);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                .body("Error updating grade calculation: " + e.getMessage());
        }
    }
    
    @PutMapping("/{id}/approve")
    @PreAuthorize("hasRole('PRINCIPAL')")
    public ResponseEntity<?> approveGrade(@PathVariable Long id) {
        try {
            GradeCalculation approvedGrade = gradeCalculationService.approveGrade(id);
            return ResponseEntity.ok(approvedGrade);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                .body("Error approving grade: " + e.getMessage());
        }
    }
    
    @PutMapping("/{id}/publish")
    @PreAuthorize("hasRole('PRINCIPAL')")
    public ResponseEntity<?> publishGrade(@PathVariable Long id) {
        try {
            GradeCalculation publishedGrade = gradeCalculationService.publishGrade(id);
            return ResponseEntity.ok(publishedGrade);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                .body("Error publishing grade: " + e.getMessage());
        }
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('FACULTY')")
    public ResponseEntity<?> deleteGradeCalculation(@PathVariable Long id) {
        try {
            gradeCalculationService.deleteGradeCalculation(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                .body("Error deleting grade calculation: " + e.getMessage());
        }
    }
}
