package com.example.erp.service;

import com.example.erp.entity.Backup;
import com.example.erp.repository.BackupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class BackupService {
    
    private final BackupRepository backupRepository;
    
    public List<Backup> getAllBackups() {
        return backupRepository.findAll();
    }
    
    public Optional<Backup> getBackupById(Long id) {
        return backupRepository.findById(id);
    }
    
    public Backup createBackup(Backup backup) {
        backup.setCreatedAt(LocalDateTime.now());
        return backupRepository.save(backup);
    }
    
    public Backup updateBackup(Long id, Backup backupDetails) {
        Backup backup = backupRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Backup not found with id: " + id));
        
        if (backupDetails.getName() != null) {
            backup.setName(backupDetails.getName());
        }
        if (backupDetails.getDescription() != null) {
            backup.setDescription(backupDetails.getDescription());
        }
        if (backupDetails.getFilePath() != null) {
            backup.setFilePath(backupDetails.getFilePath());
        }
        if (backupDetails.getSize() != null) {
            backup.setSize(backupDetails.getSize());
        }
        if (backupDetails.getStatus() != null) {
            backup.setStatus(backupDetails.getStatus());
        }
        
        return backupRepository.save(backup);
    }
    
    public void deleteBackup(Long id) {
        if (!backupRepository.existsById(id)) {
            throw new RuntimeException("Backup not found with id: " + id);
        }
        backupRepository.deleteById(id);
    }
    
    public Backup executeBackup(String name, String description) {
        Backup backup = new Backup();
        backup.setName(name);
        backup.setDescription(description);
        backup.setStatus("IN_PROGRESS");
        backup.setCreatedAt(LocalDateTime.now());
        
        // Simulate backup execution
        backup.setFilePath("/backups/" + name + "_" + System.currentTimeMillis() + ".sql");
        backup.setSize(1024L * 1024L); // 1MB simulation
        backup.setStatus("COMPLETED");
        
        return backupRepository.save(backup);
    }
}
