package com.example.erp.service;

import com.example.erp.entity.Fees;
import com.example.erp.repository.FeesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class FeesService {
    
    private final FeesRepository feesRepository;
    private final LogService logService;
    
    public List<Fees> getAllFees() {
        return feesRepository.findAll();
    }
    
    public Optional<Fees> getFeesById(Long id) {
        return feesRepository.findById(id);
    }
    
    public List<Fees> getFeesByStudent(Long studentId) {
        return feesRepository.findByStudentId(studentId);
    }
    
    public List<Fees> getFeesByPaymentStatus(Fees.PaymentStatus status) {
        return feesRepository.findByPaymentStatus(status);
    }
    
    public List<Fees> getOverdueFees() {
        return feesRepository.findByDueDateBefore(LocalDate.now());
    }
    
    public Fees createFees(Fees fees) {
        Fees savedFees = feesRepository.save(fees);
        logService.logAction(null, "CREATE", "Fees", savedFees.getId());
        return savedFees;
    }
    
    public Fees updateFees(Long id, Fees feesDetails) {
        Fees fees = feesRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Fees not found with id: " + id));
        
        fees.setAmount(feesDetails.getAmount());
        fees.setPaidAmount(feesDetails.getPaidAmount());
        fees.setDueDate(feesDetails.getDueDate());
        fees.setPaymentStatus(feesDetails.getPaymentStatus());
        
        Fees updatedFees = feesRepository.save(fees);
        logService.logAction(null, "UPDATE", "Fees", id);
        return updatedFees;
    }
    
    public Fees makePayment(Long id, BigDecimal paymentAmount) {
        Fees fees = feesRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Fees not found with id: " + id));
        
        BigDecimal newPaidAmount = fees.getPaidAmount().add(paymentAmount);
        fees.setPaidAmount(newPaidAmount);
        
        // Update payment status based on amount paid
        if (newPaidAmount.compareTo(fees.getAmount()) >= 0) {
            fees.setPaymentStatus(Fees.PaymentStatus.PAID);
        } else if (newPaidAmount.compareTo(BigDecimal.ZERO) > 0) {
            fees.setPaymentStatus(Fees.PaymentStatus.PARTIAL);
        }
        
        Fees updatedFees = feesRepository.save(fees);
        logService.logAction(null, "PAYMENT", "Fees", id);
        return updatedFees;
    }
    
    public void deleteFees(Long id) {
        Fees fees = feesRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Fees not found with id: " + id));
        
        feesRepository.delete(fees);
        logService.logAction(null, "DELETE", "Fees", id);
    }
    
    public BigDecimal getTotalPendingAmountByStudent(Long studentId) {
        BigDecimal pendingAmount = feesRepository.findTotalPendingAmountByStudentId(studentId, Fees.PaymentStatus.PAID);
        return pendingAmount != null ? pendingAmount : BigDecimal.ZERO;
    }
}
