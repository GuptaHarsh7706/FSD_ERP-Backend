package com.example.erp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "academic_calendar")
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class AcademicCalendar {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "title", nullable = false)
    private String title;
    
    @Column(name = "description")
    private String description;
    
    @Column(name = "event_date", nullable = false)
    private LocalDate eventDate;
    
    @Column(name = "end_date")
    private LocalDate endDate;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "event_type", nullable = false)
    private EventType eventType;
    
    @Column(name = "academic_year", nullable = false)
    private String academicYear;
    
    @Column(name = "semester")
    private Integer semester;
    
    @Column(name = "department_id")
    private Long departmentId;
    
    @Column(name = "is_holiday")
    private Boolean isHoliday = false;
    
    @Column(name = "created_by")
    private Long createdBy;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id", insertable = false, updatable = false)
    private Department department;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", insertable = false, updatable = false)
    private User createdByUser;
    
    public enum EventType {
        EXAM_START, EXAM_END, SEMESTER_START, SEMESTER_END, 
        HOLIDAY, ASSIGNMENT_DEADLINE, PROJECT_SUBMISSION,
        REGISTRATION_START, REGISTRATION_END, RESULT_DECLARATION,
        CONVOCATION, ORIENTATION, WORKSHOP, SEMINAR
    }
}
