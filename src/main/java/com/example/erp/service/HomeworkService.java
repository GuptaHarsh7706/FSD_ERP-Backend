package com.example.erp.service;

import com.example.erp.entity.Homework;
import com.example.erp.repository.HomeworkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class HomeworkService {
    
    private final HomeworkRepository homeworkRepository;
    
    public List<Homework> getAllHomework() {
        return homeworkRepository.findAll();
    }
    
    public Optional<Homework> getHomeworkById(Long id) {
        return homeworkRepository.findById(id);
    }
    
    public List<Homework> getHomeworkByStudent(Long studentId) {
        return homeworkRepository.findByStudentId(studentId);
    }
    
    public List<Homework> getHomeworkBySubject(Long subjectId) {
        return homeworkRepository.findBySubjectId(subjectId);
    }
    
    public Homework createHomework(Homework homework) {
        homework.setAssignedAt(LocalDateTime.now());
        return homeworkRepository.save(homework);
    }
    
    public Homework updateHomework(Long id, Homework homeworkDetails) {
        Homework homework = homeworkRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Homework not found with id: " + id));
        
        if (homeworkDetails.getTitle() != null) {
            homework.setTitle(homeworkDetails.getTitle());
        }
        if (homeworkDetails.getDescription() != null) {
            homework.setDescription(homeworkDetails.getDescription());
        }
        if (homeworkDetails.getDueDate() != null) {
            homework.setDueDate(homeworkDetails.getDueDate());
        }
        if (homeworkDetails.getMaxMarks() != null) {
            homework.setMaxMarks(homeworkDetails.getMaxMarks());
        }
        if (homeworkDetails.getStatus() != null) {
            homework.setStatus(homeworkDetails.getStatus());
        }
        
        return homeworkRepository.save(homework);
    }
    
    public void deleteHomework(Long id) {
        if (!homeworkRepository.existsById(id)) {
            throw new RuntimeException("Homework not found with id: " + id);
        }
        homeworkRepository.deleteById(id);
    }
    
    public Homework submitHomework(Long id, String submission) {
        Homework homework = homeworkRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Homework not found with id: " + id));
        
        homework.setSubmission(submission);
        homework.setSubmittedAt(LocalDateTime.now());
        homework.setStatus("SUBMITTED");
        
        return homeworkRepository.save(homework);
    }
}
