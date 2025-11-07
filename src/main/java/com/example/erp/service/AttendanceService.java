package com.example.erp.service;

import com.example.erp.entity.Attendance;
import com.example.erp.repository.AttendanceRepository;
import com.example.erp.repository.StudentRepository;
import com.example.erp.repository.SubjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class AttendanceService {
    
    private final AttendanceRepository attendanceRepository;
    private final LogService logService;
    private final StudentRepository studentRepository;
    private final SubjectRepository subjectRepository;
    
    public List<Attendance> getAllAttendance() {
        return attendanceRepository.findAll();
    }
    
    public Optional<Attendance> getAttendanceById(Long id) {
        return attendanceRepository.findById(id);
    }
    
    public List<Attendance> getAttendanceByStudent(Long studentId) {
        return attendanceRepository.findByStudentId(studentId);
    }
    
    public List<Attendance> getAttendanceBySubject(Long subjectId) {
        return attendanceRepository.findBySubjectId(subjectId);
    }
    
    public List<Attendance> getAttendanceByDate(LocalDate date) {
        return attendanceRepository.findByDate(date);
    }
    
    public List<Attendance> getAttendanceByStudentAndSubject(Long studentId, Long subjectId) {
        return attendanceRepository.findByStudentIdAndSubjectId(studentId, subjectId);
    }
    
    public List<Attendance> getAttendanceByDateRange(Long studentId, LocalDate startDate, LocalDate endDate) {
        return attendanceRepository.findByStudentIdAndDateBetween(studentId, startDate, endDate);
    }
    
    public Attendance markAttendance(Attendance attendance) {
        // Validate required fields
        if (attendance.getStudentId() == null) {
            throw new RuntimeException("Student ID is required");
        }
        if (attendance.getSubjectId() == null) {
            throw new RuntimeException("Subject ID is required");
        }
        if (attendance.getDate() == null) {
            throw new RuntimeException("Date is required");
        }
        if (attendance.getStatus() == null) {
            throw new RuntimeException("Attendance status is required");
        }
        
        // Validate student exists
        if (!studentRepository.existsById(attendance.getStudentId())) {
            throw new RuntimeException("Student not found with id: " + attendance.getStudentId());
        }
        
        // Validate subject exists
        if (!subjectRepository.existsById(attendance.getSubjectId())) {
            throw new RuntimeException("Subject not found with id: " + attendance.getSubjectId());
        }
        
        // Validate date is not in the future
        if (attendance.getDate().isAfter(LocalDate.now())) {
            throw new RuntimeException("Cannot mark attendance for future dates");
        }
        
        // Validate date is not too old (more than 30 days ago)
        if (attendance.getDate().isBefore(LocalDate.now().minusDays(30))) {
            throw new RuntimeException("Cannot mark attendance for dates older than 30 days");
        }
        
        // Check for duplicate attendance
        List<Attendance> existingAttendance = attendanceRepository.findByStudentIdAndSubjectId(
            attendance.getStudentId(), attendance.getSubjectId());
        
        for (Attendance existing : existingAttendance) {
            if (existing.getDate().equals(attendance.getDate())) {
                throw new RuntimeException("Attendance already marked for this student, subject, and date. Use update instead.");
            }
        }
        
        Attendance savedAttendance = attendanceRepository.save(attendance);
        logService.logAction(null, "CREATE", "Attendance", savedAttendance.getId());
        return savedAttendance;
    }
    
    public Attendance updateAttendance(Long id, Attendance attendanceDetails) {
        Attendance attendance = attendanceRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Attendance not found with id: " + id));
        
        // Update status if provided
        if (attendanceDetails.getStatus() != null) {
            attendance.setStatus(attendanceDetails.getStatus());
        }
        
        // Update date if provided with validation
        if (attendanceDetails.getDate() != null) {
            // Validate date is not in the future
            if (attendanceDetails.getDate().isAfter(LocalDate.now())) {
                throw new RuntimeException("Cannot set attendance date to future");
            }
            
            // Validate date is not too old
            if (attendanceDetails.getDate().isBefore(LocalDate.now().minusDays(30))) {
                throw new RuntimeException("Cannot set attendance date older than 30 days");
            }
            
            // Check if changing date would create duplicate
            if (!attendance.getDate().equals(attendanceDetails.getDate())) {
                List<Attendance> existingAttendance = attendanceRepository.findByStudentIdAndSubjectId(
                    attendance.getStudentId(), attendance.getSubjectId());
                
                for (Attendance existing : existingAttendance) {
                    if (!existing.getId().equals(id) && existing.getDate().equals(attendanceDetails.getDate())) {
                        throw new RuntimeException("Attendance already exists for this student, subject, and date");
                    }
                }
            }
            
            attendance.setDate(attendanceDetails.getDate());
        }
        
        Attendance updatedAttendance = attendanceRepository.save(attendance);
        logService.logAction(null, "UPDATE", "Attendance", id);
        return updatedAttendance;
    }
    
    public void deleteAttendance(Long id) {
        Attendance attendance = attendanceRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Attendance not found with id: " + id));
        
        attendanceRepository.delete(attendance);
        logService.logAction(null, "DELETE", "Attendance", id);
    }
    
    public long getPresentCountByStudentAndSubject(Long studentId, Long subjectId) {
        return attendanceRepository.countPresentByStudentAndSubject(studentId, subjectId, Attendance.AttendanceStatus.PRESENT);
    }
    
    public double getAttendancePercentage(Long studentId, Long subjectId) {
        List<Attendance> totalAttendance = attendanceRepository.findByStudentIdAndSubjectId(studentId, subjectId);
        long presentCount = attendanceRepository.countPresentByStudentAndSubject(studentId, subjectId, Attendance.AttendanceStatus.PRESENT);
        
        if (totalAttendance.isEmpty()) {
            return 0.0;
        }
        
        return (double) presentCount / totalAttendance.size() * 100;
    }
}
