package com.example.erp.service;

import com.example.erp.entity.Subject;
import com.example.erp.repository.SubjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class SubjectService {
    
    private final SubjectRepository subjectRepository;
    private final LogService logService;
    
    public List<Subject> getAllSubjects() {
        return subjectRepository.findAll();
    }
    
    public Optional<Subject> getSubjectById(Long id) {
        return subjectRepository.findById(id);
    }
    
    public Optional<Subject> getSubjectByCode(String code) {
        return subjectRepository.findByCode(code);
    }
    
    public List<Subject> getSubjectsByDepartment(Long departmentId) {
        return subjectRepository.findByDepartmentId(departmentId);
    }
    
    public List<Subject> getSubjectsBySemester(Integer semester) {
        return subjectRepository.findBySemester(semester);
    }
    
    public List<Subject> getSubjectsByDepartmentAndSemester(Long departmentId, Integer semester) {
        return subjectRepository.findByDepartmentIdAndSemester(departmentId, semester);
    }
    
    public Subject createSubject(Subject subject) {
        if (subject.getCode() != null && subjectRepository.existsByCode(subject.getCode())) {
            throw new RuntimeException("Subject with code " + subject.getCode() + " already exists");
        }
        
        Subject savedSubject = subjectRepository.save(subject);
        logService.logAction(null, "CREATE", "Subject", savedSubject.getId());
        return savedSubject;
    }
    
    public Subject updateSubject(Long id, Subject subjectDetails) {
        Subject subject = subjectRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Subject not found with id: " + id));
        
        subject.setName(subjectDetails.getName());
        subject.setCode(subjectDetails.getCode());
        subject.setSemester(subjectDetails.getSemester());
        subject.setDepartmentId(subjectDetails.getDepartmentId());
        
        Subject updatedSubject = subjectRepository.save(subject);
        logService.logAction(null, "UPDATE", "Subject", id);
        return updatedSubject;
    }
    
    public void deleteSubject(Long id) {
        Subject subject = subjectRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Subject not found with id: " + id));
        
        subjectRepository.delete(subject);
        logService.logAction(null, "DELETE", "Subject", id);
    }
    
    public List<Subject> searchSubjectsByName(String name) {
        return subjectRepository.findByNameContaining(name);
    }
    
    public boolean existsByCode(String code) {
        return subjectRepository.existsByCode(code);
    }
}
