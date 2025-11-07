package com.example.erp.service;

import com.example.erp.entity.Department;
import com.example.erp.repository.DepartmentRepository;
import com.example.erp.repository.FacultyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class DepartmentService {
    
    private final DepartmentRepository departmentRepository;
    private final LogService logService;
    private final FacultyRepository facultyRepository;
    
    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }
    
    public Optional<Department> getDepartmentById(Long id) {
        return departmentRepository.findById(id);
    }
    
    public Optional<Department> getDepartmentByName(String name) {
        return departmentRepository.findByName(name);
    }
    
    public List<Department> getDepartmentsByHead(Long headId) {
        return departmentRepository.findByHeadId(headId);
    }
    
    public Department createDepartment(Department department) {
        // Validate name is provided
        if (department.getName() == null || department.getName().trim().isEmpty()) {
            throw new RuntimeException("Department name is required");
        }
        
        // Check name uniqueness
        if (departmentRepository.existsByName(department.getName())) {
            throw new RuntimeException("Department with name " + department.getName() + " already exists");
        }
        
        // Validate head if provided
        if (department.getHeadId() != null) {
            if (!facultyRepository.existsById(department.getHeadId())) {
                throw new RuntimeException("Faculty not found with id: " + department.getHeadId());
            }
        }
        
        Department savedDepartment = departmentRepository.save(department);
        logService.logAction(null, "CREATE", "Department", savedDepartment.getId());
        return savedDepartment;
    }
    
    public Department updateDepartment(Long id, Department departmentDetails) {
        Department department = departmentRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Department not found with id: " + id));
        
        // Update name with uniqueness check
        if (departmentDetails.getName() != null && !department.getName().equals(departmentDetails.getName())) {
            if (departmentRepository.existsByName(departmentDetails.getName())) {
                throw new RuntimeException("Department with name '" + departmentDetails.getName() + "' already exists");
            }
            department.setName(departmentDetails.getName());
        }
        
        // Update head with validation
        if (departmentDetails.getHeadId() != null) {
            // Validate head exists in faculty table
            if (!facultyRepository.existsById(departmentDetails.getHeadId())) {
                throw new RuntimeException("Faculty not found with id: " + departmentDetails.getHeadId());
            }
            department.setHeadId(departmentDetails.getHeadId());
        }
        
        Department updatedDepartment = departmentRepository.save(department);
        logService.logAction(null, "UPDATE", "Department", id);
        return updatedDepartment;
    }
    
    public void deleteDepartment(Long id) {
        Department department = departmentRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Department not found with id: " + id));
        
        departmentRepository.delete(department);
        logService.logAction(null, "DELETE", "Department", id);
    }
    
    public List<Department> searchDepartmentsByName(String name) {
        return departmentRepository.findByNameContaining(name);
    }
    
    public boolean existsByName(String name) {
        return departmentRepository.existsByName(name);
    }
}
