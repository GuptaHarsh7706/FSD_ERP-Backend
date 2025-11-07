package com.example.erp.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "backup")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Backup {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name must not exceed 100 characters")
    @Column(name = "name", nullable = false)
    private String name;
    
    @Size(max = 500, message = "Description must not exceed 500 characters")
    @Column(name = "description")
    private String description;
    
    @Size(max = 500, message = "File path must not exceed 500 characters")
    @Column(name = "file_path")
    private String filePath;
    
    @Column(name = "size")
    private Long size;
    
    @Size(max = 50, message = "Status must not exceed 50 characters")
    @Column(name = "status")
    private String status;
    
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}
