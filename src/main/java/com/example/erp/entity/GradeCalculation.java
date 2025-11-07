package com.example.erp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "grade_calculation")
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class GradeCalculation {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "student_id", nullable = false)
    private Long studentId;
    
    @Column(name = "subject_id", nullable = false)
    private Long subjectId;
    
    @Column(name = "academic_year", nullable = false)
    private String academicYear;
    
    @Column(name = "semester", nullable = false)
    private Integer semester;
    
    @Column(name = "internal_marks", precision = 5, scale = 2)
    private BigDecimal internalMarks;
    
    @Column(name = "external_marks", precision = 5, scale = 2)
    private BigDecimal externalMarks;
    
    @Column(name = "assignment_marks", precision = 5, scale = 2)
    private BigDecimal assignmentMarks;
    
    @Column(name = "attendance_percentage", precision = 5, scale = 2)
    private BigDecimal attendancePercentage;
    
    @Column(name = "total_marks", precision = 5, scale = 2)
    private BigDecimal totalMarks;
    
    @Column(name = "grade", length = 5)
    private String grade;
    
    @Column(name = "grade_points", precision = 3, scale = 2)
    private BigDecimal gradePoints;
    
    @Column(name = "credits")
    private Integer credits;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private GradeStatus status = GradeStatus.PENDING;
    
    @Column(name = "calculated_at")
    private LocalDateTime calculatedAt;
    
    @Column(name = "calculated_by")
    private Long calculatedBy;
    
    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", insertable = false, updatable = false)
    private Student student;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id", insertable = false, updatable = false)
    private Subject subject;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "calculated_by", insertable = false, updatable = false)
    private User calculatedByUser;
    
    public enum GradeStatus {
        PENDING, CALCULATED, APPROVED, PUBLISHED
    }
}
