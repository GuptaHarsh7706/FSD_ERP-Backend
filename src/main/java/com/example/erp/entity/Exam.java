package com.example.erp.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "exam")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Exam {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "subject_id", nullable = false)
    private Long subjectId;
    
    @Column(name = "department_id", nullable = false)
    private Long departmentId;
    
    private LocalDate date;
    
    @Size(max = 255, message = "Exam name must not exceed 255 characters")
    private String name;
    
    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id", insertable = false, updatable = false)
    private Subject subject;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id", insertable = false, updatable = false)
    private Department department;
    
    @OneToMany(mappedBy = "exam", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Marks> marks;
}
