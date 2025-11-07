package com.example.erp.controller;

import com.example.erp.entity.Fees;
import com.example.erp.service.FeesService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/fees")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class FeesController {

    private final FeesService feesService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRINCIPAL') or hasRole('STAFF')")
    public ResponseEntity<List<Fees>> getAllFees() {
        List<Fees> fees = feesService.getAllFees();
        return ResponseEntity.ok(fees);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRINCIPAL') or hasRole('STAFF')")
    public ResponseEntity<Fees> getFeesById(@PathVariable Long id) {
        return feesService.getFeesById(id)
                .map(fees -> ResponseEntity.ok(fees))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/student/{studentId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('FACULTY') or @studentService.getStudentById(#studentId).get().userId == authentication.principal.userId")
    public ResponseEntity<List<Fees>> getFeesByStudent(@PathVariable Long studentId) {
        List<Fees> fees = feesService.getFeesByStudent(studentId);
        return ResponseEntity.ok(fees);
    }

    @GetMapping("/status/{status}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRINCIPAL') or hasRole('STAFF')")
    public ResponseEntity<List<Fees>> getFeesByPaymentStatus(@PathVariable Fees.PaymentStatus status) {
        List<Fees> fees = feesService.getFeesByPaymentStatus(status);
        return ResponseEntity.ok(fees);
    }

    @GetMapping("/overdue")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRINCIPAL') or hasRole('STAFF')")
    public ResponseEntity<List<Fees>> getOverdueFees() {
        List<Fees> fees = feesService.getOverdueFees();
        return ResponseEntity.ok(fees);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRINCIPAL') or hasRole('STAFF')")
    public ResponseEntity<Fees> createFees(@Valid @RequestBody Fees fees) {
        Fees createdFees = feesService.createFees(fees);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdFees);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRINCIPAL') or hasRole('STAFF')")
    public ResponseEntity<Fees> updateFees(@PathVariable Long id, @Valid @RequestBody Fees feesDetails) {
        try {
            Fees updatedFees = feesService.updateFees(id, feesDetails);
            return ResponseEntity.ok(updatedFees);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{id}/payment")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRINCIPAL') or hasRole('STAFF')")
    public ResponseEntity<Fees> makePayment(@PathVariable Long id, @RequestParam BigDecimal amount) {
        try {
            Fees updatedFees = feesService.makePayment(id, amount);
            return ResponseEntity.ok(updatedFees);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteFees(@PathVariable Long id) {
        try {
            feesService.deleteFees(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/student/{studentId}/pending-amount")
    @PreAuthorize("hasRole('ADMIN') or hasRole('FACULTY') or @studentService.getStudentById(#studentId).get().userId == authentication.principal.userId")
    public ResponseEntity<BigDecimal> getTotalPendingAmount(@PathVariable Long studentId) {
        BigDecimal pendingAmount = feesService.getTotalPendingAmountByStudent(studentId);
        return ResponseEntity.ok(pendingAmount);
    }
}
