package com.example.erp.service;

import com.example.erp.entity.GradeCalculation;
import com.example.erp.repository.GradeCalculationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class GradeCalculationService {
    
    private final GradeCalculationRepository gradeCalculationRepository;
    
    public List<GradeCalculation> getAllGradeCalculations() {
        return gradeCalculationRepository.findAll();
    }
    
    public Optional<GradeCalculation> getGradeCalculationById(Long id) {
        return gradeCalculationRepository.findById(id);
    }
    
    public List<GradeCalculation> getGradeCalculationsByStudent(Long studentId) {
        return gradeCalculationRepository.findByStudentId(studentId);
    }
    
    public List<GradeCalculation> getGradeCalculationsBySubject(Long subjectId) {
        return gradeCalculationRepository.findBySubjectId(subjectId);
    }
    
    public Double calculateGPA(Long studentId, String academicYear) {
        return gradeCalculationRepository.calculateGPA(studentId, academicYear, GradeCalculation.GradeStatus.PUBLISHED);
    }
    
    public GradeCalculation createGradeCalculation(GradeCalculation gradeCalculation) {
        calculateTotalMarksAndGrade(gradeCalculation);
        gradeCalculation.setCalculatedAt(LocalDateTime.now());
        gradeCalculation.setStatus(GradeCalculation.GradeStatus.CALCULATED);
        return gradeCalculationRepository.save(gradeCalculation);
    }
    
    public GradeCalculation updateGradeCalculation(Long id, GradeCalculation gradeDetails) {
        GradeCalculation gradeCalculation = gradeCalculationRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Grade calculation not found with id: " + id));
        
        if (gradeDetails.getInternalMarks() != null) {
            gradeCalculation.setInternalMarks(gradeDetails.getInternalMarks());
        }
        if (gradeDetails.getExternalMarks() != null) {
            gradeCalculation.setExternalMarks(gradeDetails.getExternalMarks());
        }
        if (gradeDetails.getAssignmentMarks() != null) {
            gradeCalculation.setAssignmentMarks(gradeDetails.getAssignmentMarks());
        }
        if (gradeDetails.getAttendancePercentage() != null) {
            gradeCalculation.setAttendancePercentage(gradeDetails.getAttendancePercentage());
        }
        if (gradeDetails.getCredits() != null) {
            gradeCalculation.setCredits(gradeDetails.getCredits());
        }
        
        calculateTotalMarksAndGrade(gradeCalculation);
        gradeCalculation.setCalculatedAt(LocalDateTime.now());
        
        return gradeCalculationRepository.save(gradeCalculation);
    }
    
    public void deleteGradeCalculation(Long id) {
        if (!gradeCalculationRepository.existsById(id)) {
            throw new RuntimeException("Grade calculation not found with id: " + id);
        }
        gradeCalculationRepository.deleteById(id);
    }
    
    public GradeCalculation approveGrade(Long id) {
        GradeCalculation gradeCalculation = gradeCalculationRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Grade calculation not found with id: " + id));
        
        gradeCalculation.setStatus(GradeCalculation.GradeStatus.APPROVED);
        return gradeCalculationRepository.save(gradeCalculation);
    }
    
    public GradeCalculation publishGrade(Long id) {
        GradeCalculation gradeCalculation = gradeCalculationRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Grade calculation not found with id: " + id));
        
        gradeCalculation.setStatus(GradeCalculation.GradeStatus.PUBLISHED);
        return gradeCalculationRepository.save(gradeCalculation);
    }
    
    private void calculateTotalMarksAndGrade(GradeCalculation gradeCalculation) {
        BigDecimal internal = gradeCalculation.getInternalMarks() != null ? gradeCalculation.getInternalMarks() : BigDecimal.ZERO;
        BigDecimal external = gradeCalculation.getExternalMarks() != null ? gradeCalculation.getExternalMarks() : BigDecimal.ZERO;
        BigDecimal assignment = gradeCalculation.getAssignmentMarks() != null ? gradeCalculation.getAssignmentMarks() : BigDecimal.ZERO;
        
        // Calculate total marks (internal 30% + external 60% + assignment 10%)
        BigDecimal totalMarks = internal.multiply(new BigDecimal("0.30"))
            .add(external.multiply(new BigDecimal("0.60")))
            .add(assignment.multiply(new BigDecimal("0.10")))
            .setScale(2, RoundingMode.HALF_UP);
        
        gradeCalculation.setTotalMarks(totalMarks);
        
        // Calculate grade and grade points based on total marks
        String grade;
        BigDecimal gradePoints;
        
        if (totalMarks.compareTo(new BigDecimal("90")) >= 0) {
            grade = "A+";
            gradePoints = new BigDecimal("10.00");
        } else if (totalMarks.compareTo(new BigDecimal("80")) >= 0) {
            grade = "A";
            gradePoints = new BigDecimal("9.00");
        } else if (totalMarks.compareTo(new BigDecimal("70")) >= 0) {
            grade = "B+";
            gradePoints = new BigDecimal("8.00");
        } else if (totalMarks.compareTo(new BigDecimal("60")) >= 0) {
            grade = "B";
            gradePoints = new BigDecimal("7.00");
        } else if (totalMarks.compareTo(new BigDecimal("50")) >= 0) {
            grade = "C";
            gradePoints = new BigDecimal("6.00");
        } else if (totalMarks.compareTo(new BigDecimal("40")) >= 0) {
            grade = "D";
            gradePoints = new BigDecimal("5.00");
        } else {
            grade = "F";
            gradePoints = new BigDecimal("0.00");
        }
        
        gradeCalculation.setGrade(grade);
        gradeCalculation.setGradePoints(gradePoints);
    }
}
