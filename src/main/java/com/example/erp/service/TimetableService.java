package com.example.erp.service;

import com.example.erp.entity.Timetable;
import com.example.erp.repository.TimetableRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class TimetableService {
    
    private final TimetableRepository timetableRepository;
    
    public List<Timetable> getAllTimetables() {
        return timetableRepository.findAll();
    }
    
    public Optional<Timetable> getTimetableById(Long id) {
        return timetableRepository.findById(id);
    }
    
    public List<Timetable> getTimetableByDepartment(Long departmentId) {
        return timetableRepository.findByDepartmentId(departmentId);
    }
    
    public List<Timetable> getTimetableBySubject(Long subjectId) {
        return timetableRepository.findBySubjectId(subjectId);
    }
    
    public List<Timetable> getTimetableByFaculty(Long facultyId) {
        return timetableRepository.findByFacultyId(facultyId);
    }
    
    public Timetable createTimetable(Timetable timetable) {
        timetable.setCreatedAt(LocalDateTime.now());
        return timetableRepository.save(timetable);
    }
    
    public Timetable updateTimetable(Long id, Timetable timetableDetails) {
        Timetable timetable = timetableRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Timetable not found with id: " + id));
        
        if (timetableDetails.getDayOfWeek() != null) {
            timetable.setDayOfWeek(timetableDetails.getDayOfWeek());
        }
        if (timetableDetails.getStartTime() != null) {
            timetable.setStartTime(timetableDetails.getStartTime());
        }
        if (timetableDetails.getEndTime() != null) {
            timetable.setEndTime(timetableDetails.getEndTime());
        }
        if (timetableDetails.getClassroom() != null) {
            timetable.setClassroom(timetableDetails.getClassroom());
        }
        if (timetableDetails.getAcademicYear() != null) {
            timetable.setAcademicYear(timetableDetails.getAcademicYear());
        }
        if (timetableDetails.getSemester() != null) {
            timetable.setSemester(timetableDetails.getSemester());
        }
        
        timetable.setUpdatedAt(LocalDateTime.now());
        return timetableRepository.save(timetable);
    }
    
    public void deleteTimetable(Long id) {
        if (!timetableRepository.existsById(id)) {
            throw new RuntimeException("Timetable not found with id: " + id);
        }
        timetableRepository.deleteById(id);
    }
}
