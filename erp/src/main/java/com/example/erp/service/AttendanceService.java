package com.example.erp.service;

import com.example.erp.entity.Attendance;
import com.example.erp.repository.AttendanceRepository;
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
        Attendance savedAttendance = attendanceRepository.save(attendance);
        logService.logAction(null, "CREATE", "Attendance", savedAttendance.getId());
        return savedAttendance;
    }
    
    public Attendance updateAttendance(Long id, Attendance attendanceDetails) {
        Attendance attendance = attendanceRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Attendance not found with id: " + id));
        
        attendance.setStatus(attendanceDetails.getStatus());
        attendance.setDate(attendanceDetails.getDate());
        
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
