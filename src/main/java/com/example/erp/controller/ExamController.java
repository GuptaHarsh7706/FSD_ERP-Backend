package com.example.erp.controller;

import com.example.erp.entity.Exam;
import com.example.erp.service.ExamService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/exams")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ExamController {

    private final ExamService examService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('FACULTY') or hasRole('STUDENT')")
    public ResponseEntity<List<Exam>> getAllExams() {
        List<Exam> exams = examService.getAllExams();
        return ResponseEntity.ok(exams);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('FACULTY') or hasRole('STUDENT')")
    public ResponseEntity<Exam> getExamById(@PathVariable Long id) {
        return examService.getExamById(id)
                .map(exam -> ResponseEntity.ok(exam))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/subject/{subjectId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('FACULTY') or hasRole('STUDENT')")
    public ResponseEntity<List<Exam>> getExamsBySubject(@PathVariable Long subjectId) {
        List<Exam> exams = examService.getExamsBySubject(subjectId);
        return ResponseEntity.ok(exams);
    }

    @GetMapping("/department/{departmentId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('FACULTY') or hasRole('STUDENT')")
    public ResponseEntity<List<Exam>> getExamsByDepartment(@PathVariable Long departmentId) {
        List<Exam> exams = examService.getExamsByDepartment(departmentId);
        return ResponseEntity.ok(exams);
    }

    @GetMapping("/date/{date}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('FACULTY') or hasRole('STUDENT')")
    public ResponseEntity<List<Exam>> getExamsByDate(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<Exam> exams = examService.getExamsByDate(date);
        return ResponseEntity.ok(exams);
    }

    @GetMapping("/daterange")
    @PreAuthorize("hasRole('ADMIN') or hasRole('FACULTY') or hasRole('STUDENT')")
    public ResponseEntity<List<Exam>> getExamsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<Exam> exams = examService.getExamsByDateRange(startDate, endDate);
        return ResponseEntity.ok(exams);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('FACULTY')")
    public ResponseEntity<Exam> createExam(@Valid @RequestBody Exam exam) {
        Exam createdExam = examService.createExam(exam);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdExam);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('FACULTY')")
    public ResponseEntity<Exam> updateExam(@PathVariable Long id, @Valid @RequestBody Exam examDetails) {
        try {
            Exam updatedExam = examService.updateExam(id, examDetails);
            return ResponseEntity.ok(updatedExam);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteExam(@PathVariable Long id) {
        try {
            examService.deleteExam(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/search")
    @PreAuthorize("hasRole('ADMIN') or hasRole('FACULTY') or hasRole('STUDENT')")
    public ResponseEntity<List<Exam>> searchExams(@RequestParam String name) {
        List<Exam> exams = examService.searchExamsByName(name);
        return ResponseEntity.ok(exams);
    }
}
