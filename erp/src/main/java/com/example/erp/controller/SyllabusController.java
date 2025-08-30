package com.example.erp.controller;

import com.example.erp.entity.Syllabus;
import com.example.erp.service.SyllabusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/syllabus")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class SyllabusController {
    
    private final SyllabusService syllabusService;
    
    @GetMapping
    public ResponseEntity<List<Syllabus>> getAllSyllabus() {
        List<Syllabus> syllabus = syllabusService.getAllSyllabus();
        return ResponseEntity.ok(syllabus);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Syllabus> getSyllabusById(@PathVariable Long id) {
        return syllabusService.getSyllabusById(id)
            .map(syllabus -> ResponseEntity.ok(syllabus))
            .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/subject/{subjectId}")
    public ResponseEntity<List<Syllabus>> getSyllabusBySubject(@PathVariable Long subjectId) {
        List<Syllabus> syllabus = syllabusService.getSyllabusBySubject(subjectId);
        return ResponseEntity.ok(syllabus);
    }
    
    @GetMapping("/department/{departmentId}")
    public ResponseEntity<List<Syllabus>> getSyllabusByDepartment(@PathVariable Long departmentId) {
        List<Syllabus> syllabus = syllabusService.getSyllabusByDepartment(departmentId);
        return ResponseEntity.ok(syllabus);
    }
    
    @PostMapping
    @PreAuthorize("hasRole('FACULTY')")
    public ResponseEntity<?> createSyllabus(@Valid @RequestBody Syllabus syllabus) {
        try {
            Syllabus createdSyllabus = syllabusService.createSyllabus(syllabus);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdSyllabus);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                .body("Error creating syllabus: " + e.getMessage());
        }
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('FACULTY')")
    public ResponseEntity<?> updateSyllabus(@PathVariable Long id, @Valid @RequestBody Syllabus syllabus) {
        try {
            Syllabus updatedSyllabus = syllabusService.updateSyllabus(id, syllabus);
            return ResponseEntity.ok(updatedSyllabus);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                .body("Error updating syllabus: " + e.getMessage());
        }
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('FACULTY')")
    public ResponseEntity<?> deleteSyllabus(@PathVariable Long id) {
        try {
            syllabusService.deleteSyllabus(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                .body("Error deleting syllabus: " + e.getMessage());
        }
    }
}
