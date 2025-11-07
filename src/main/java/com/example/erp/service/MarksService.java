package com.example.erp.service;

import com.example.erp.entity.Marks;
import com.example.erp.repository.MarksRepository;
import com.example.erp.repository.StudentRepository;
import com.example.erp.repository.ExamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class MarksService {
    
    private final MarksRepository marksRepository;
    private final LogService logService;
    private final StudentRepository studentRepository;
    private final ExamRepository examRepository;
    
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
        if (marks.getStudentId() == null) {
            throw new RuntimeException("Student ID is required");
        }
        if (marks.getExamId() == null) {
            throw new RuntimeException("Exam ID is required");
        }
        if (marks.getMarksObtained() == null) {
            throw new RuntimeException("Marks obtained is required");
        }
        if (marks.getTotalMarks() == null) {
            throw new RuntimeException("Total marks is required");
        }
        
        if (!studentRepository.existsById(marks.getStudentId())) {
            throw new RuntimeException("Student not found with id: " + marks.getStudentId());
        }
        
        if (!examRepository.existsById(marks.getExamId())) {
            throw new RuntimeException("Exam not found with id: " + marks.getExamId());
        }
        
        if (marks.getMarksObtained().compareTo(BigDecimal.ZERO) < 0) {
            throw new RuntimeException("Marks obtained cannot be negative");
        }
        if (marks.getTotalMarks().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Total marks must be greater than zero");
        }
        
        if (marks.getMarksObtained().compareTo(marks.getTotalMarks()) > 0) {
            throw new RuntimeException("Marks obtained (" + marks.getMarksObtained() + 
                ") cannot be greater than total marks (" + marks.getTotalMarks() + ")");
        }
        
        List<Marks> existingMarks = marksRepository.findByStudentIdAndExamId(marks.getStudentId(), marks.getExamId());
        if (!existingMarks.isEmpty()) {
            throw new RuntimeException("Marks already exist for this student and exam. Use update instead.");
        }
        
        Marks savedMarks = marksRepository.save(marks);
        logService.logAction(null, "CREATE", "Marks", savedMarks.getId());
        return savedMarks;
    }
    
    public Marks updateMarks(Long id, Marks marksDetails) {
        Marks marks = marksRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Marks not found with id: " + id));
        
        if (marksDetails.getMarksObtained() != null) {
            if (marksDetails.getMarksObtained().compareTo(BigDecimal.ZERO) < 0) {
                throw new RuntimeException("Marks obtained cannot be negative");
            }
            
            BigDecimal totalMarks = marksDetails.getTotalMarks() != null ? 
                marksDetails.getTotalMarks() : marks.getTotalMarks();
            
            if (marksDetails.getMarksObtained().compareTo(totalMarks) > 0) {
                throw new RuntimeException("Marks obtained (" + marksDetails.getMarksObtained() + 
                    ") cannot be greater than total marks (" + totalMarks + ")");
            }
            
            marks.setMarksObtained(marksDetails.getMarksObtained());
        }
        
        if (marksDetails.getTotalMarks() != null) {
            if (marksDetails.getTotalMarks().compareTo(BigDecimal.ZERO) <= 0) {
                throw new RuntimeException("Total marks must be greater than zero");
            }
            
            BigDecimal marksObtained = marksDetails.getMarksObtained() != null ? 
                marksDetails.getMarksObtained() : marks.getMarksObtained();
            
            if (marksObtained.compareTo(marksDetails.getTotalMarks()) > 0) {
                throw new RuntimeException("Marks obtained (" + marksObtained + 
                    ") cannot be greater than total marks (" + marksDetails.getTotalMarks() + ")");
            }
            
            marks.setTotalMarks(marksDetails.getTotalMarks());
        }
        
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
