package com.example.erp.repository;

import com.example.erp.entity.Fees;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface FeesRepository extends JpaRepository<Fees, Long> {
    
    List<Fees> findByStudentId(Long studentId);
    
    List<Fees> findByPaymentStatus(Fees.PaymentStatus paymentStatus);
    
    List<Fees> findByDueDateBefore(LocalDate date);
    
    @Query("SELECT f FROM Fees f WHERE f.studentId = :studentId AND f.paymentStatus = :status")
    List<Fees> findByStudentIdAndPaymentStatus(@Param("studentId") Long studentId, 
                                               @Param("status") Fees.PaymentStatus status);
    
    @Query("SELECT SUM(f.amount - f.paidAmount) FROM Fees f WHERE f.studentId = :studentId AND f.paymentStatus != :paidStatus")
    BigDecimal findTotalPendingAmountByStudentId(@Param("studentId") Long studentId, @Param("paidStatus") Fees.PaymentStatus paidStatus);
}
