package com.example.erp.service;

import com.example.erp.entity.Log;
import com.example.erp.repository.LogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class LogService {

    private final LogRepository logRepository;

    public void logAction(Long userId, String action, String entityType, Long entityId) {
        try {
            Log log = new Log();
            log.setUserId(userId);
            log.setAction(action);
            log.setEntityType(entityType);
            log.setEntityId(entityId);
            log.setTimestamp(LocalDateTime.now());

            logRepository.save(log);
        } catch (Exception e) {
            // Log the error but don't fail the main operation
            System.err.println("Failed to log action: " + e.getMessage());
        }
    }

    public List<Log> getAllLogs() {
        return logRepository.findAllByOrderByTimestampDesc();
    }

    public List<Log> getLogsByUser(Long userId) {
        return logRepository.findByUserId(userId);
    }

    public List<Log> getLogsByAction(String action) {
        return logRepository.findByAction(action);
    }

    public List<Log> getLogsByEntityType(String entityType) {
        return logRepository.findByEntityType(entityType);
    }

    public List<Log> getLogsByDateRange(LocalDateTime startTime, LocalDateTime endTime) {
        return logRepository.findByTimestampBetween(startTime, endTime);
    }
    
    public java.util.Optional<Log> getLogById(Long id) {
        return logRepository.findById(id);
    }
    
    public Log createLog(Log log) {
        log.setTimestamp(LocalDateTime.now());
        return logRepository.save(log);
    }
    
    public void deleteLog(Long id) {
        if (!logRepository.existsById(id)) {
            throw new RuntimeException("Log not found with id: " + id);
        }
        logRepository.deleteById(id);
    }
}
