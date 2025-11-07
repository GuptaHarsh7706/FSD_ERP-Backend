package com.example.erp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "homework")
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Homework {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "title", nullable = false)
    private String title;
    
    @Column(name = "subject_id", nullable = false)
    private Long subjectId;
    
    @Column(name = "faculty_id", nullable = false)
    private Long facultyId;
    
    @Column(name = "student_id")
    private Long studentId;
    
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    
    @Column(name = "due_date")
    private LocalDate dueDate;
    
    @Column(name = "max_marks")
    private Integer maxMarks;
    
    @Column(name = "status")
    private String status = "ASSIGNED";
    
    @Column(name = "assigned_at")
    private LocalDateTime assignedAt;
    
    @Column(name = "submitted_at")
    private LocalDateTime submittedAt;
    
    @Column(name = "submission", columnDefinition = "TEXT")
    private String submission;
    
    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id", insertable = false, updatable = false)
    private Subject subject;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "faculty_id", insertable = false, updatable = false)
    private Faculty faculty;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", insertable = false, updatable = false)
    private Student student;
}
