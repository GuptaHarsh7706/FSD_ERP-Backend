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
        
        enrollment.setStatus(Enrollment.EnrollmentStatus.COMPLETED);
        enrollment.setGrade(grade);
        enrollment.setCompletedAt(LocalDateTime.now());
        
        return enrollmentRepository.save(enrollment);
    }
}
