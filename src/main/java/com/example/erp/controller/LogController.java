package com.example.erp.controller;

import com.example.erp.entity.Log;
import com.example.erp.service.LogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/logs")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class LogController {
    
    private final LogService logService;
    
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Log>> getAllLogs() {
        List<Log> logs = logService.getAllLogs();
        return ResponseEntity.ok(logs);
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Log> getLogById(@PathVariable Long id) {
        return logService.getLogById(id)
            .map(log -> ResponseEntity.ok(log))
            .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('ADMIN') or @userService.isCurrentUser(#userId)")
    public ResponseEntity<List<Log>> getLogsByUser(@PathVariable Long userId) {
        List<Log> logs = logService.getLogsByUser(userId);
        return ResponseEntity.ok(logs);
    }
    
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createLog(@Valid @RequestBody Log log) {
        try {
            Log createdLog = logService.createLog(log);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdLog);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                .body("Error creating log: " + e.getMessage());
        }
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteLog(@PathVariable Long id) {
        try {
            logService.deleteLog(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                .body("Error deleting log: " + e.getMessage());
        }
    }
}
