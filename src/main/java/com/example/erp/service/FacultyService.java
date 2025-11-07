package com.example.erp.service;

import com.example.erp.entity.Faculty;
import com.example.erp.repository.FacultyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class FacultyService {
    
    private final FacultyRepository facultyRepository;
    private final LogService logService;
    
    public List<Faculty> getAllFaculty() {
        return facultyRepository.findAll();
    }
    
    public Optional<Faculty> getFacultyById(Long id) {
        return facultyRepository.findById(id);
    }
    
    public Optional<Faculty> getFacultyByUserId(Long userId) {
        return facultyRepository.findByUserId(userId);
    }
    
    public List<Faculty> getFacultyByDesignation(String designation) {
        return facultyRepository.findByDesignation(designation);
    }
    
    public List<Faculty> getFacultyByDepartment(Long departmentId) {
        return facultyRepository.findByDepartmentId(departmentId);
    }
    
    public Faculty createFaculty(Faculty faculty) {
        // Validate required fields
        if (faculty.getUserId() == null) {
            throw new RuntimeException("User ID is required");
        }
        
        // Check if user already has a faculty record
        if (facultyRepository.findByUserId(faculty.getUserId()).isPresent()) {
            throw new RuntimeException("User already has a faculty record");
        }
        
        Faculty savedFaculty = facultyRepository.save(faculty);
        logService.logAction(faculty.getUserId(), "CREATE", "Faculty", savedFaculty.getId());
        return savedFaculty;
    }
    
    public Faculty updateFaculty(Long id, Faculty facultyDetails) {
        Faculty faculty = facultyRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Faculty not found with id: " + id));
        
        faculty.setDesignation(facultyDetails.getDesignation());
        
        Faculty updatedFaculty = facultyRepository.save(faculty);
        logService.logAction(faculty.getUserId(), "UPDATE", "Faculty", id);
        return updatedFaculty;
    }
    
    public void deleteFaculty(Long id) {
        Faculty faculty = facultyRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Faculty not found with id: " + id));
        
        // Check if faculty is head of any department
        if (faculty.getHeadedDepartments() != null && !faculty.getHeadedDepartments().isEmpty()) {
            throw new RuntimeException("Cannot delete faculty who is head of one or more departments. Reassign department head first.");
        }
        
        facultyRepository.delete(faculty);
        logService.logAction(faculty.getUserId(), "DELETE", "Faculty", id);
    }
    
    public List<Faculty> searchFacultyByDesignation(String designation) {
        return facultyRepository.findByDesignationContaining(designation);
    }
}
