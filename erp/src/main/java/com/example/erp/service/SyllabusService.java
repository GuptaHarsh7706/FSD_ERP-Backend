package com.example.erp.service;

import com.example.erp.entity.Syllabus;
import com.example.erp.repository.SyllabusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class SyllabusService {
    
    private final SyllabusRepository syllabusRepository;
    
    public List<Syllabus> getAllSyllabus() {
        return syllabusRepository.findAll();
    }
    
    public Optional<Syllabus> getSyllabusById(Long id) {
        return syllabusRepository.findById(id);
    }
    
    public List<Syllabus> getSyllabusBySubject(Long subjectId) {
        return syllabusRepository.findBySubjectId(subjectId);
    }
    
    public List<Syllabus> getSyllabusByDepartment(Long departmentId) {
        return syllabusRepository.findByDepartmentId(departmentId);
    }
    
    public Syllabus createSyllabus(Syllabus syllabus) {
        syllabus.setCreatedAt(LocalDateTime.now());
        return syllabusRepository.save(syllabus);
    }
    
    public Syllabus updateSyllabus(Long id, Syllabus syllabusDetails) {
        Syllabus syllabus = syllabusRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Syllabus not found with id: " + id));
        
        if (syllabusDetails.getTitle() != null) {
            syllabus.setTitle(syllabusDetails.getTitle());
        }
        if (syllabusDetails.getDescription() != null) {
            syllabus.setDescription(syllabusDetails.getDescription());
        }
        if (syllabusDetails.getContent() != null) {
            syllabus.setContent(syllabusDetails.getContent());
        }
        if (syllabusDetails.getAcademicYear() != null) {
            syllabus.setAcademicYear(syllabusDetails.getAcademicYear());
        }
        if (syllabusDetails.getSemester() != null) {
            syllabus.setSemester(syllabusDetails.getSemester());
        }
        if (syllabusDetails.getStatus() != null) {
            syllabus.setStatus(syllabusDetails.getStatus());
        }
        
        syllabus.setUpdatedAt(LocalDateTime.now());
        return syllabusRepository.save(syllabus);
    }
    
    public void deleteSyllabus(Long id) {
        if (!syllabusRepository.existsById(id)) {
            throw new RuntimeException("Syllabus not found with id: " + id);
        }
        syllabusRepository.deleteById(id);
    }
}
