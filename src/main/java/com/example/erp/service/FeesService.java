package com.example.erp.service;

import com.example.erp.entity.Fees;
import com.example.erp.repository.FeesRepository;
import com.example.erp.repository.StudentRepository;
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
    private final StudentRepository studentRepository;
    
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
        // Validate required fields
        if (fees.getStudentId() == null) {
            throw new RuntimeException("Student ID is required");
        }
        if (fees.getAmount() == null) {
            throw new RuntimeException("Fee amount is required");
        }
        
        // Validate student exists
        if (!studentRepository.existsById(fees.getStudentId())) {
            throw new RuntimeException("Student not found with id: " + fees.getStudentId());
        }
        
        // Validate amount is positive
        if (fees.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Fee amount must be greater than zero");
        }
        
        // Validate due date is not in the past
        if (fees.getDueDate() != null && fees.getDueDate().isBefore(LocalDate.now())) {
            throw new RuntimeException("Due date cannot be in the past");
        }
        
        // Initialize paid amount if not set
        if (fees.getPaidAmount() == null) {
            fees.setPaidAmount(BigDecimal.ZERO);
        }
        
        // Validate paid amount is not negative
        if (fees.getPaidAmount().compareTo(BigDecimal.ZERO) < 0) {
            throw new RuntimeException("Paid amount cannot be negative");
        }
        
        // Validate paid amount does not exceed total amount
        if (fees.getPaidAmount().compareTo(fees.getAmount()) > 0) {
            throw new RuntimeException("Paid amount cannot exceed total fee amount");
        }
        
        // Set initial payment status if not set
        if (fees.getPaymentStatus() == null) {
            if (fees.getPaidAmount().compareTo(BigDecimal.ZERO) == 0) {
                fees.setPaymentStatus(Fees.PaymentStatus.PENDING);
            } else if (fees.getPaidAmount().compareTo(fees.getAmount()) >= 0) {
                fees.setPaymentStatus(Fees.PaymentStatus.PAID);
            } else {
                fees.setPaymentStatus(Fees.PaymentStatus.PARTIAL);
            }
        }
        
        Fees savedFees = feesRepository.save(fees);
        logService.logAction(null, "CREATE", "Fees", savedFees.getId());
        return savedFees;
    }
    
    public Fees updateFees(Long id, Fees feesDetails) {
        Fees fees = feesRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Fees not found with id: " + id));
        
        // Update amount with validation
        if (feesDetails.getAmount() != null) {
            if (feesDetails.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
                throw new RuntimeException("Fee amount must be greater than zero");
            }
            
            // Check if new amount is less than already paid amount
            BigDecimal currentPaidAmount = fees.getPaidAmount() != null ? fees.getPaidAmount() : BigDecimal.ZERO;
            if (feesDetails.getAmount().compareTo(currentPaidAmount) < 0) {
                throw new RuntimeException("New fee amount cannot be less than already paid amount (" + currentPaidAmount + ")");
            }
            
            fees.setAmount(feesDetails.getAmount());
        }
        
        // Update paid amount with validation
        if (feesDetails.getPaidAmount() != null) {
            if (feesDetails.getPaidAmount().compareTo(BigDecimal.ZERO) < 0) {
                throw new RuntimeException("Paid amount cannot be negative");
            }
            
            BigDecimal totalAmount = fees.getAmount();
            if (feesDetails.getPaidAmount().compareTo(totalAmount) > 0) {
                throw new RuntimeException("Paid amount cannot exceed total fee amount (" + totalAmount + ")");
            }
            
            fees.setPaidAmount(feesDetails.getPaidAmount());
        }
        
        // Update due date with validation
        if (feesDetails.getDueDate() != null) {
            fees.setDueDate(feesDetails.getDueDate());
        }
        
        // Recalculate payment status based on amounts
        BigDecimal currentPaidAmount = fees.getPaidAmount() != null ? fees.getPaidAmount() : BigDecimal.ZERO;
        if (currentPaidAmount.compareTo(BigDecimal.ZERO) == 0) {
            fees.setPaymentStatus(Fees.PaymentStatus.PENDING);
        } else if (currentPaidAmount.compareTo(fees.getAmount()) >= 0) {
            fees.setPaymentStatus(Fees.PaymentStatus.PAID);
        } else {
            fees.setPaymentStatus(Fees.PaymentStatus.PARTIAL);
        }
        
        Fees updatedFees = feesRepository.save(fees);
        logService.logAction(null, "UPDATE", "Fees", id);
        return updatedFees;
    }
    
    public Fees makePayment(Long id, BigDecimal paymentAmount) {
        Fees fees = feesRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Fees not found with id: " + id));
        
        // Validate payment amount
        if (paymentAmount == null) {
            throw new RuntimeException("Payment amount is required");
        }
        
        if (paymentAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Payment amount must be greater than zero");
        }
        
        // Check if fees are already fully paid
        if (fees.getPaymentStatus() == Fees.PaymentStatus.PAID) {
            throw new RuntimeException("Fees are already fully paid");
        }
        
        // Calculate new paid amount
        BigDecimal currentPaidAmount = fees.getPaidAmount() != null ? fees.getPaidAmount() : BigDecimal.ZERO;
        BigDecimal newPaidAmount = currentPaidAmount.add(paymentAmount);
        
        // Check for overpayment
        if (newPaidAmount.compareTo(fees.getAmount()) > 0) {
            throw new RuntimeException("Payment amount (" + paymentAmount + 
                ") would result in overpayment. Remaining amount: " + 
                fees.getAmount().subtract(currentPaidAmount));
        }
        
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
