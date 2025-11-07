package com.example.erp.repository;

import com.example.erp.entity.Backup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BackupRepository extends JpaRepository<Backup, Long> {
    
    List<Backup> findByCreatedAtBetween(LocalDateTime startTime, LocalDateTime endTime);
    
    @Query("SELECT b FROM Backup b WHERE b.filePath LIKE CONCAT('%', :path, '%')")
    List<Backup> findByFilePathContaining(@Param("path") String path);
    
    List<Backup> findAllByOrderByCreatedAtDesc();
}
