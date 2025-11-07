package com.example.erp.controller;

import com.example.erp.entity.Department;
import com.example.erp.service.DepartmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/departments")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class DepartmentController {

    private final DepartmentService departmentService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('FACULTY') or hasRole('STUDENT')")
    public ResponseEntity<List<Department>> getAllDepartments() {
        List<Department> departments = departmentService.getAllDepartments();
        return ResponseEntity.ok(departments);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('FACULTY') or hasRole('STUDENT')")
    public ResponseEntity<Department> getDepartmentById(@PathVariable Long id) {
        return departmentService.getDepartmentById(id)
                .map(department -> ResponseEntity.ok(department))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/name/{name}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('FACULTY')")
    public ResponseEntity<Department> getDepartmentByName(@PathVariable String name) {
        return departmentService.getDepartmentByName(name)
                .map(department -> ResponseEntity.ok(department))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/head/{headId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('FACULTY')")
    public ResponseEntity<List<Department>> getDepartmentsByHead(@PathVariable Long headId) {
        List<Department> departments = departmentService.getDepartmentsByHead(headId);
        return ResponseEntity.ok(departments);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Department> createDepartment(@Valid @RequestBody Department department) {
        try {
            Department createdDepartment = departmentService.createDepartment(department);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdDepartment);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Department> updateDepartment(@PathVariable Long id,
            @Valid @RequestBody Department departmentDetails) {
        try {
            Department updatedDepartment = departmentService.updateDepartment(id, departmentDetails);
            return ResponseEntity.ok(updatedDepartment);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteDepartment(@PathVariable Long id) {
        try {
            departmentService.deleteDepartment(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/search")
    @PreAuthorize("hasRole('ADMIN') or hasRole('FACULTY')")
    public ResponseEntity<List<Department>> searchDepartments(@RequestParam String name) {
        List<Department> departments = departmentService.searchDepartmentsByName(name);
        return ResponseEntity.ok(departments);
    }
}
