package com.example.erp.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "student")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Student {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "user_id", nullable = false)
    private Long userId;
    
    @NotBlank(message = "PRN number is required")
    @Size(max = 50, message = "PRN number must not exceed 50 characters")
    @Column(name = "prn_number", unique = true, nullable = false)
    private String prnNumber;
    
    @Size(max = 10, message = "Batch must not exceed 10 characters")
    private String batch;
    
    private Integer semester;
    
    @Size(max = 10, message = "Division must not exceed 10 characters")
    private String division;
    
    @Size(max = 20, message = "Roll number must not exceed 20 characters")
    @Column(name = "roll_number")
    private String rollNumber;
    
    // Relationships
    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;
    
    @JsonIgnore
    @OneToMany(mappedBy = "student", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<Attendance> attendances;
    
    @JsonIgnore
    @OneToMany(mappedBy = "student", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<Marks> marks;
    
    @JsonIgnore
    @OneToMany(mappedBy = "student", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<Fees> fees;
    
    @JsonIgnore
    @OneToMany(mappedBy = "student", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<Enrollment> enrollments;
    
    @JsonIgnore
    @OneToMany(mappedBy = "student", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<Homework> homework;
}
