package com.example.erp.repository;

import com.example.erp.entity.Accreditation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface AccreditationRepository extends JpaRepository<Accreditation, Long> {
    
    List<Accreditation> findByDepartmentId(Long departmentId);
    
    List<Accreditation> findByAccreditingBody(String bodyName);
    
    @Query("SELECT a FROM Accreditation a WHERE a.score >= :minScore")
    List<Accreditation> findByScoreGreaterThanEqual(@Param("minScore") BigDecimal minScore);
    
    @Query("SELECT a FROM Accreditation a WHERE a.accreditingBody LIKE CONCAT('%', :bodyName, '%')")
    List<Accreditation> findByAccreditingBodyContaining(@Param("bodyName") String bodyName);
}
