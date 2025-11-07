package com.example.erp.repository;

import com.example.erp.entity.Log;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface LogRepository extends JpaRepository<Log, Long> {
    
    List<Log> findByUserId(Long userId);
    
    List<Log> findByAction(String action);
    
    List<Log> findByEntityType(String entityType);
    
    List<Log> findByEntityId(Long entityId);
    
    List<Log> findByTimestampBetween(LocalDateTime startTime, LocalDateTime endTime);
    
    @Query("SELECT l FROM Log l WHERE l.userId = :userId AND l.timestamp BETWEEN :startTime AND :endTime")
    List<Log> findByUserIdAndTimestampBetween(@Param("userId") Long userId, 
                                             @Param("startTime") LocalDateTime startTime, 
                                             @Param("endTime") LocalDateTime endTime);
    
    List<Log> findAllByOrderByTimestampDesc();
}
