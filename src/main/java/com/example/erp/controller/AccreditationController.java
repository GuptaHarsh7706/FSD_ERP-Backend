package com.example.erp.controller;

import com.example.erp.entity.Accreditation;
import com.example.erp.service.AccreditationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/accreditation")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AccreditationController {
    
    private final AccreditationService accreditationService;
    
    @GetMapping
    public ResponseEntity<List<Accreditation>> getAllAccreditations() {
        List<Accreditation> accreditations = accreditationService.getAllAccreditations();
        return ResponseEntity.ok(accreditations);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Accreditation> getAccreditationById(@PathVariable Long id) {
        return accreditationService.getAccreditationById(id)
            .map(accreditation -> ResponseEntity.ok(accreditation))
            .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRINCIPAL')")
    public ResponseEntity<?> createAccreditation(@Valid @RequestBody Accreditation accreditation) {
        try {
            Accreditation createdAccreditation = accreditationService.createAccreditation(accreditation);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdAccreditation);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                .body("Error creating accreditation: " + e.getMessage());
        }
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRINCIPAL')")
    public ResponseEntity<?> updateAccreditation(@PathVariable Long id, @Valid @RequestBody Accreditation accreditation) {
        try {
            Accreditation updatedAccreditation = accreditationService.updateAccreditation(id, accreditation);
            return ResponseEntity.ok(updatedAccreditation);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                .body("Error updating accreditation: " + e.getMessage());
        }
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteAccreditation(@PathVariable Long id) {
        try {
            accreditationService.deleteAccreditation(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                .body("Error deleting accreditation: " + e.getMessage());
        }
    }
}
