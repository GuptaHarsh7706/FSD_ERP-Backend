package com.example.erp.controller;

import com.example.erp.entity.Backup;
import com.example.erp.service.BackupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/backup")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class BackupController {
    
    private final BackupService backupService;
    
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Backup>> getAllBackups() {
        List<Backup> backups = backupService.getAllBackups();
        return ResponseEntity.ok(backups);
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Backup> getBackupById(@PathVariable Long id) {
        return backupService.getBackupById(id)
            .map(backup -> ResponseEntity.ok(backup))
            .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createBackup(@Valid @RequestBody Backup backup) {
        try {
            Backup createdBackup = backupService.createBackup(backup);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdBackup);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                .body("Error creating backup: " + e.getMessage());
        }
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateBackup(@PathVariable Long id, @Valid @RequestBody Backup backup) {
        try {
            Backup updatedBackup = backupService.updateBackup(id, backup);
            return ResponseEntity.ok(updatedBackup);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                .body("Error updating backup: " + e.getMessage());
        }
    }
    
    @PostMapping("/{id}/execute")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> executeBackup(@PathVariable Long id) {
        try {
            Optional<Backup> backupOpt = backupService.getBackupById(id);
            if (!backupOpt.isPresent()) {
                return ResponseEntity.notFound().build();
            }
            Backup backup = backupOpt.get();
            Backup result = backupService.executeBackup(backup.getName(), backup.getDescription());
            return ResponseEntity.ok(result);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                .body("Error executing backup: " + e.getMessage());
        }
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteBackup(@PathVariable Long id) {
        try {
            backupService.deleteBackup(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                .body("Error deleting backup: " + e.getMessage());
        }
    }
}
