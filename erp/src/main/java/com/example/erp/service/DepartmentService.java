package com.example.erp.service;

import com.example.erp.entity.Department;
import com.example.erp.repository.DepartmentRepository;
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
        if (departmentRepository.existsByName(department.getName())) {
            throw new RuntimeException("Department with name " + department.getName() + " already exists");
        }
        
        Department savedDepartment = departmentRepository.save(department);
        logService.logAction(null, "CREATE", "Department", savedDepartment.getId());
        return savedDepartment;
    }
    
    public Department updateDepartment(Long id, Department departmentDetails) {
        Department department = departmentRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Department not found with id: " + id));
        
        department.setName(departmentDetails.getName());
        department.setHeadId(departmentDetails.getHeadId());
        
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
