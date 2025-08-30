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
        
        facultyRepository.delete(faculty);
        logService.logAction(faculty.getUserId(), "DELETE", "Faculty", id);
    }
    
    public List<Faculty> searchFacultyByDesignation(String designation) {
        return facultyRepository.findByDesignationContaining(designation);
    }
}
