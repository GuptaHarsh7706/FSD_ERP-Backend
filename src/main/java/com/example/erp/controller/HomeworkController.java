package com.example.erp.controller;

import com.example.erp.entity.Homework;
import com.example.erp.service.HomeworkService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/homework")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class HomeworkController {
    
    private final HomeworkService homeworkService;
    
    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('FACULTY') or hasRole('STUDENT')")
    public ResponseEntity<List<Homework>> getAllHomework() {
        List<Homework> homework = homeworkService.getAllHomework();
        return ResponseEntity.ok(homework);
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('FACULTY') or hasRole('STUDENT')")
    public ResponseEntity<Homework> getHomeworkById(@PathVariable Long id) {
        return homeworkService.getHomeworkById(id)
            .map(homework -> ResponseEntity.ok(homework))
            .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/student/{studentId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('FACULTY') or hasRole('STUDENT')")
    public ResponseEntity<List<Homework>> getHomeworkByStudent(@PathVariable Long studentId) {
        List<Homework> homework = homeworkService.getHomeworkByStudent(studentId);
        return ResponseEntity.ok(homework);
    }
    
    @GetMapping("/subject/{subjectId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('FACULTY')")
    public ResponseEntity<List<Homework>> getHomeworkBySubject(@PathVariable Long subjectId) {
        List<Homework> homework = homeworkService.getHomeworkBySubject(subjectId);
        return ResponseEntity.ok(homework);
    }
    
    @PostMapping
    @PreAuthorize("hasRole('FACULTY')")
    public ResponseEntity<?> createHomework(@Valid @RequestBody Homework homework) {
        try {
            Homework createdHomework = homeworkService.createHomework(homework);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdHomework);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                .body("Error creating homework: " + e.getMessage());
        }
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('FACULTY')")
    public ResponseEntity<?> updateHomework(@PathVariable Long id, @Valid @RequestBody Homework homework) {
        try {
            Homework updatedHomework = homeworkService.updateHomework(id, homework);
            return ResponseEntity.ok(updatedHomework);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                .body("Error updating homework: " + e.getMessage());
        }
    }
    
    @PutMapping("/{id}/submit")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<?> submitHomework(@PathVariable Long id, @RequestParam String submission) {
        try {
            Homework submittedHomework = homeworkService.submitHomework(id, submission);
            return ResponseEntity.ok(submittedHomework);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                .body("Error submitting homework: " + e.getMessage());
        }
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('FACULTY')")
    public ResponseEntity<?> deleteHomework(@PathVariable Long id) {
        try {
            homeworkService.deleteHomework(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                .body("Error deleting homework: " + e.getMessage());
        }
    }
}
