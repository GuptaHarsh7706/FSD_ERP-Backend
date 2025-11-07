package com.example.erp.controller;

import com.example.erp.entity.Faculty;
import com.example.erp.service.FacultyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/faculty")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class FacultyController {

    private final FacultyService facultyService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('FACULTY')")
    public ResponseEntity<List<Faculty>> getAllFaculty() {
        List<Faculty> faculty = facultyService.getAllFaculty();
        return ResponseEntity.ok(faculty);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('FACULTY')")
    public ResponseEntity<Faculty> getFacultyById(@PathVariable Long id) {
        return facultyService.getFacultyById(id)
                .map(faculty -> ResponseEntity.ok(faculty))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('FACULTY')")
    public ResponseEntity<Faculty> getFacultyByUserId(@PathVariable Long userId) {
        return facultyService.getFacultyByUserId(userId)
                .map(faculty -> ResponseEntity.ok(faculty))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/designation/{designation}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('FACULTY')")
    public ResponseEntity<List<Faculty>> getFacultyByDesignation(@PathVariable String designation) {
        List<Faculty> faculty = facultyService.getFacultyByDesignation(designation);
        return ResponseEntity.ok(faculty);
    }

    @GetMapping("/department/{departmentId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('FACULTY')")
    public ResponseEntity<List<Faculty>> getFacultyByDepartment(@PathVariable Long departmentId) {
        List<Faculty> faculty = facultyService.getFacultyByDepartment(departmentId);
        return ResponseEntity.ok(faculty);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRINCIPAL')")
    public ResponseEntity<Faculty> createFaculty(@Valid @RequestBody Faculty faculty) {
        Faculty createdFaculty = facultyService.createFaculty(faculty);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdFaculty);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @facultyService.getFacultyById(#id).get().userId == authentication.principal.userId")
    public ResponseEntity<Faculty> updateFaculty(@PathVariable Long id, @Valid @RequestBody Faculty facultyDetails) {
        try {
            Faculty updatedFaculty = facultyService.updateFaculty(id, facultyDetails);
            return ResponseEntity.ok(updatedFaculty);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteFaculty(@PathVariable Long id) {
        try {
            facultyService.deleteFaculty(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/search")
    @PreAuthorize("hasRole('ADMIN') or hasRole('FACULTY')")
    public ResponseEntity<List<Faculty>> searchFaculty(@RequestParam String designation) {
        List<Faculty> faculty = facultyService.searchFacultyByDesignation(designation);
        return ResponseEntity.ok(faculty);
    }
}
