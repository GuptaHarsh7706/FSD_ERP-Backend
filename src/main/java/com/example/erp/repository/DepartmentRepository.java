package com.example.erp.repository;

import com.example.erp.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
    
    Optional<Department> findByName(String name);
    
    List<Department> findByHeadId(Long headId);
    
    @Query("SELECT d FROM Department d WHERE d.name LIKE CONCAT('%', :name, '%')")
    List<Department> findByNameContaining(@Param("name") String name);
    
    boolean existsByName(String name);
}
