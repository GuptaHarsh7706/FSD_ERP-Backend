package com.example.erp.service;

import com.example.erp.entity.Enrollment;
import com.example.erp.repository.EnrollmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class EnrollmentService {
    
    private final EnrollmentRepository enrollmentRepository;
    
    public List<Enrollment> getAllEnrollments() {
        return enrollmentRepository.findAll();
    }
    
    public Optional<Enrollment> getEnrollmentById(Long id) {
        return enrollmentRepository.findById(id);
    }
    
    public List<Enrollment> getEnrollmentsByStudent(Long studentId) {
        return enrollmentRepository.findByStudentId(studentId);
    }
    
    public List<Enrollment> getEnrollmentsBySubject(Long subjectId) {
        return enrollmentRepository.findBySubjectId(subjectId);
    }
    
    public List<Enrollment> getEnrollmentsByAcademicYear(String academicYear) {
        return enrollmentRepository.findByAcademicYear(academicYear);
    }
    
    public List<Enrollment> getEnrollmentsByDepartment(Long departmentId) {
        return enrollmentRepository.findByDepartmentId(departmentId);
    }
    
    public Enrollment createEnrollment(Enrollment enrollment) {
        // Validate required fields
        if (enrollment.getStudent() == null || enrollment.getStudent().getId() == null) {
            throw new RuntimeException("Student is required");
        }
        if (enrollment.getSubject() == null || enrollment.getSubject().getId() == null) {
            throw new RuntimeException("Subject is required");
        }
        if (enrollment.getAcademicYear() == null || enrollment.getAcademicYear().trim().isEmpty()) {
            throw new RuntimeException("Academic year is required");
        }
        if (enrollment.getSemester() == null) {
            throw new RuntimeException("Semester is required");
        }
        
        // Validate semester range
        if (enrollment.getSemester() < 1 || enrollment.getSemester() > 8) {
            throw new RuntimeException("Semester must be between 1 and 8");
        }
        
        // Check for duplicate enrollment
        List<Enrollment> existingEnrollments = enrollmentRepository.findByStudentIdAndSubjectIdAndAcademicYearAndSemester(
            enrollment.getStudent().getId(), 
            enrollment.getSubject().getId(),
            enrollment.getAcademicYear(),
            enrollment.getSemester()
        );
        
        if (!existingEnrollments.isEmpty()) {
            throw new RuntimeException("Student is already enrolled in this subject for the given academic year and semester");
        }
        
        enrollment.setEnrolledAt(LocalDateTime.now());
        enrollment.setStatus(Enrollment.EnrollmentStatus.ACTIVE);
        return enrollmentRepository.save(enrollment);
    }
    
    public Enrollment updateEnrollment(Long id, Enrollment enrollmentDetails) {
        Enrollment enrollment = enrollmentRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Enrollment not found with id: " + id));
        
        if (enrollmentDetails.getStatus() != null) {
            enrollment.setStatus(enrollmentDetails.getStatus());
        }
        if (enrollmentDetails.getGrade() != null) {
            enrollment.setGrade(enrollmentDetails.getGrade());
        }
        if (enrollmentDetails.getCredits() != null) {
            enrollment.setCredits(enrollmentDetails.getCredits());
        }
        
        // Mark as completed if status is COMPLETED
        if (enrollmentDetails.getStatus() == Enrollment.EnrollmentStatus.COMPLETED) {
            enrollment.setCompletedAt(LocalDateTime.now());
        }
        
        return enrollmentRepository.save(enrollment);
    }
    
    public void deleteEnrollment(Long id) {
        if (!enrollmentRepository.existsById(id)) {
            throw new RuntimeException("Enrollment not found with id: " + id);
        }
        enrollmentRepository.deleteById(id);
    }
    
    public Enrollment completeEnrollment(Long id, String grade) {
        Enrollment enrollment = enrollmentRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Enrollment not found with id: " + id));
        
        // Check if already completed
        if (enrollment.getStatus() == Enrollment.EnrollmentStatus.COMPLETED) {
            throw new RuntimeException("Enrollment is already completed");
        }
        
        // Validate grade format
        if (grade == null || grade.trim().isEmpty()) {
            throw new RuntimeException("Grade is required to complete enrollment");
        }
        
        // Validate grade is in acceptable format (A, B, C, D, F, A+, B+, etc.)
        String gradeUpper = grade.trim().toUpperCase();
        if (!gradeUpper.matches("^[A-F][+-]?$") && !gradeUpper.equals("PASS") && !gradeUpper.equals("FAIL")) {
            throw new RuntimeException("Invalid grade format. Use A, B, C, D, F (with optional + or -), PASS, or FAIL");
        }
        
        enrollment.setStatus(Enrollment.EnrollmentStatus.COMPLETED);
        enrollment.setGrade(gradeUpper);
        enrollment.setCompletedAt(LocalDateTime.now());
        
        return enrollmentRepository.save(enrollment);
    }
}
