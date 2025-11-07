package com.example.erp.service;

import com.example.erp.entity.Exam;
import com.example.erp.repository.ExamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ExamService {
    
    private final ExamRepository examRepository;
    private final LogService logService;
    
    public List<Exam> getAllExams() {
        return examRepository.findAll();
    }
    
    public Optional<Exam> getExamById(Long id) {
        return examRepository.findById(id);
    }
    
    public List<Exam> getExamsBySubject(Long subjectId) {
        return examRepository.findBySubjectId(subjectId);
    }
    
    public List<Exam> getExamsByDepartment(Long departmentId) {
        return examRepository.findByDepartmentId(departmentId);
    }
    
    public List<Exam> getExamsByDate(LocalDate date) {
        return examRepository.findByDate(date);
    }
    
    public List<Exam> getExamsByDateRange(LocalDate startDate, LocalDate endDate) {
        return examRepository.findByDateBetween(startDate, endDate);
    }
    
    public Exam createExam(Exam exam) {
        Exam savedExam = examRepository.save(exam);
        logService.logAction(null, "CREATE", "Exam", savedExam.getId());
        return savedExam;
    }
    
    public Exam updateExam(Long id, Exam examDetails) {
        Exam exam = examRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Exam not found with id: " + id));
        
        exam.setName(examDetails.getName());
        exam.setDate(examDetails.getDate());
        exam.setSubjectId(examDetails.getSubjectId());
        exam.setDepartmentId(examDetails.getDepartmentId());
        
        Exam updatedExam = examRepository.save(exam);
        logService.logAction(null, "UPDATE", "Exam", id);
        return updatedExam;
    }
    
    public void deleteExam(Long id) {
        Exam exam = examRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Exam not found with id: " + id));
        
        examRepository.delete(exam);
        logService.logAction(null, "DELETE", "Exam", id);
    }
    
    public List<Exam> searchExamsByName(String name) {
        return examRepository.findByNameContaining(name);
    }
}
