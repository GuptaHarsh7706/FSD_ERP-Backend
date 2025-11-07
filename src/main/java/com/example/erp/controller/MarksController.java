package com.example.erp.controller;

import com.example.erp.entity.Marks;
import com.example.erp.service.MarksService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/marks")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class MarksController {

    private final MarksService marksService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRINCIPAL') or hasRole('FACULTY')")
    public ResponseEntity<List<Marks>> getAllMarks() {
        List<Marks> marks = marksService.getAllMarks();
        return ResponseEntity.ok(marks);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRINCIPAL') or hasRole('FACULTY')")
    public ResponseEntity<Marks> getMarksById(@PathVariable Long id) {
        return marksService.getMarksById(id)
                .map(marks -> ResponseEntity.ok(marks))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/student/{studentId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('FACULTY') or @studentService.getStudentById(#studentId).get().userId == authentication.principal.userId")
    public ResponseEntity<List<Marks>> getMarksByStudent(@PathVariable Long studentId) {
        List<Marks> marks = marksService.getMarksByStudent(studentId);
        return ResponseEntity.ok(marks);
    }

    @GetMapping("/exam/{examId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRINCIPAL') or hasRole('FACULTY')")
    public ResponseEntity<List<Marks>> getMarksByExam(@PathVariable Long examId) {
        List<Marks> marks = marksService.getMarksByExam(examId);
        return ResponseEntity.ok(marks);
    }

    @GetMapping("/subject/{subjectId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRINCIPAL') or hasRole('FACULTY')")
    public ResponseEntity<List<Marks>> getMarksBySubject(@PathVariable Long subjectId) {
        List<Marks> marks = marksService.getMarksBySubject(subjectId);
        return ResponseEntity.ok(marks);
    }

    @GetMapping("/student/{studentId}/subject/{subjectId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('FACULTY') or @studentService.getStudentById(#studentId).get().userId == authentication.principal.userId")
    public ResponseEntity<List<Marks>> getMarksByStudentAndSubject(
            @PathVariable Long studentId, @PathVariable Long subjectId) {
        List<Marks> marks = marksService.getMarksByStudentAndSubject(studentId, subjectId);
        return ResponseEntity.ok(marks);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRINCIPAL') or hasRole('FACULTY')")
    public ResponseEntity<Marks> createMarks(@Valid @RequestBody Marks marks) {
        Marks createdMarks = marksService.createMarks(marks);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdMarks);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRINCIPAL') or hasRole('FACULTY')")
    public ResponseEntity<Marks> updateMarks(@PathVariable Long id, @Valid @RequestBody Marks marksDetails) {
        try {
            Marks updatedMarks = marksService.updateMarks(id, marksDetails);
            return ResponseEntity.ok(updatedMarks);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteMarks(@PathVariable Long id) {
        try {
            marksService.deleteMarks(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/student/{studentId}/average")
    @PreAuthorize("hasRole('ADMIN') or hasRole('FACULTY') or @studentService.getStudentById(#studentId).get().userId == authentication.principal.userId")
    public ResponseEntity<Double> getAverageMarksByStudent(@PathVariable Long studentId) {
        Double average = marksService.getAverageMarksByStudent(studentId);
        return ResponseEntity.ok(average != null ? average : 0.0);
    }
}
