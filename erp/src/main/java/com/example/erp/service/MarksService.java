package com.example.erp.service;

import com.example.erp.entity.Marks;
import com.example.erp.repository.MarksRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class MarksService {
    
    private final MarksRepository marksRepository;
    private final LogService logService;
    
    public List<Marks> getAllMarks() {
        return marksRepository.findAll();
    }
    
    public Optional<Marks> getMarksById(Long id) {
        return marksRepository.findById(id);
    }
    
    public List<Marks> getMarksByStudent(Long studentId) {
        return marksRepository.findByStudentId(studentId);
    }
    
    public List<Marks> getMarksByExam(Long examId) {
        return marksRepository.findByExamId(examId);
    }
    
    public List<Marks> getMarksBySubject(Long subjectId) {
        return marksRepository.findBySubjectId(subjectId);
    }
    
    public List<Marks> getMarksByStudentAndSubject(Long studentId, Long subjectId) {
        return marksRepository.findByStudentIdAndSubjectId(studentId, subjectId);
    }
    
    public Marks createMarks(Marks marks) {
        Marks savedMarks = marksRepository.save(marks);
        logService.logAction(null, "CREATE", "Marks", savedMarks.getId());
        return savedMarks;
    }
    
    public Marks updateMarks(Long id, Marks marksDetails) {
        Marks marks = marksRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Marks not found with id: " + id));
        
        marks.setMarksObtained(marksDetails.getMarksObtained());
        marks.setTotalMarks(marksDetails.getTotalMarks());
        
        Marks updatedMarks = marksRepository.save(marks);
        logService.logAction(null, "UPDATE", "Marks", id);
        return updatedMarks;
    }
    
    public void deleteMarks(Long id) {
        Marks marks = marksRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Marks not found with id: " + id));
        
        marksRepository.delete(marks);
        logService.logAction(null, "DELETE", "Marks", id);
    }
    
    public Double getAverageMarksByStudent(Long studentId) {
        return marksRepository.findAverageMarksByStudentId(studentId);
    }
}
