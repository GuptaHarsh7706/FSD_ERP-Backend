package com.example.erp.service;

import com.example.erp.entity.Student;
import com.example.erp.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class StudentService {
    
    private final StudentRepository studentRepository;
    private final LogService logService;
    
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }
    
    public Optional<Student> getStudentById(Long id) {
        return studentRepository.findById(id);
    }
    
    public Optional<Student> getStudentByPrnNumber(String prnNumber) {
        return studentRepository.findByPrnNumber(prnNumber);
    }
    
    public Optional<Student> getStudentByUserId(Long userId) {
        return studentRepository.findByUserId(userId);
    }
    
    public List<Student> getStudentsByBatch(String batch) {
        return studentRepository.findByBatch(batch);
    }
    
    public List<Student> getStudentsBySemester(Integer semester) {
        return studentRepository.findBySemester(semester);
    }
    
    public List<Student> getStudentsByBatchAndSemester(String batch, Integer semester) {
        return studentRepository.findByBatchAndSemester(batch, semester);
    }
    
    public List<Student> getStudentsByDepartment(Long departmentId) {
        return studentRepository.findByDepartmentId(departmentId);
    }
    
    public Student createStudent(Student student) {
        if (studentRepository.existsByPrnNumber(student.getPrnNumber())) {
            throw new RuntimeException("Student with PRN " + student.getPrnNumber() + " already exists");
        }
        
        if (student.getUserId() == null) {
            throw new RuntimeException("User ID is required");
        }
        
        if (studentRepository.findByUserId(student.getUserId()).isPresent()) {
            throw new RuntimeException("User already has a student record");
        }
        
        if (student.getSemester() != null && (student.getSemester() < 1 || student.getSemester() > 8)) {
            throw new RuntimeException("Semester must be between 1 and 8");
        }
        
        Student savedStudent = studentRepository.save(student);
        logService.logAction(student.getUserId(), "CREATE", "Student", savedStudent.getId());
        return savedStudent;
    }
    
    public Student updateStudent(Long id, Student studentDetails) {
        Student student = studentRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Student not found with id: " + id));
        
        if (studentDetails.getPrnNumber() != null && !student.getPrnNumber().equals(studentDetails.getPrnNumber())) {
            if (studentRepository.existsByPrnNumber(studentDetails.getPrnNumber())) {
                throw new RuntimeException("PRN number already exists for another student");
            }
            student.setPrnNumber(studentDetails.getPrnNumber());
        }
        
        if (studentDetails.getBatch() != null) {
            student.setBatch(studentDetails.getBatch());
        }
        
        if (studentDetails.getSemester() != null) {
            if (studentDetails.getSemester() < 1 || studentDetails.getSemester() > 8) {
                throw new RuntimeException("Semester must be between 1 and 8");
            }
            student.setSemester(studentDetails.getSemester());
        }
        
        if (studentDetails.getDivision() != null) {
            student.setDivision(studentDetails.getDivision());
        }
        
        if (studentDetails.getRollNumber() != null) {
            student.setRollNumber(studentDetails.getRollNumber());
        }
        
        Student updatedStudent = studentRepository.save(student);
        logService.logAction(student.getUserId(), "UPDATE", "Student", id);
        return updatedStudent;
    }
    
    public void deleteStudent(Long id) {
        Student student = studentRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Student not found with id: " + id));
        
        studentRepository.delete(student);
        logService.logAction(student.getUserId(), "DELETE", "Student", id);
    }
    
    public long getStudentCountBySemester(Integer semester) {
        return studentRepository.countBySemester(semester);
    }
    
    public boolean existsByPrnNumber(String prnNumber) {
        return studentRepository.existsByPrnNumber(prnNumber);
    }
}
