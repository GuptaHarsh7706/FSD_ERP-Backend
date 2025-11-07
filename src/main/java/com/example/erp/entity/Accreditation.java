package com.example.erp.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "accreditation")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Accreditation {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Size(max = 100, message = "Name must not exceed 100 characters")
    @Column(name = "name", nullable = false)
    private String name;
    
    @Size(max = 500, message = "Description must not exceed 500 characters")
    @Column(name = "description")
    private String description;
    
    @Size(max = 255, message = "Accrediting body must not exceed 255 characters")
    @Column(name = "accrediting_body")
    private String accreditingBody;
    
    @Column(name = "valid_from")
    private java.time.LocalDate validFrom;
    
    @Column(name = "valid_to")
    private java.time.LocalDate validTo;
    
    @Size(max = 50, message = "Status must not exceed 50 characters")
    @Column(name = "status")
    private String status;
    
    @Column(name = "score", precision = 5, scale = 2)
    private java.math.BigDecimal score;
    
    @Size(max = 1000, message = "Remarks must not exceed 1000 characters")
    @Column(name = "remarks")
    private String remarks;

    @Column(name = "department_id")
    private Long departmentId;
    
    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id", insertable = false, updatable = false)
    private Department department;
}
