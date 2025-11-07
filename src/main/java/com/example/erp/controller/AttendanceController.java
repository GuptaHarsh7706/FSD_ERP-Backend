package com.example.erp.controller;

import com.example.erp.entity.Attendance;
import com.example.erp.service.AttendanceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/attendance")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AttendanceController {

    private final AttendanceService attendanceService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRINCIPAL') or hasRole('FACULTY')")
    public ResponseEntity<List<Attendance>> getAllAttendance() {
        List<Attendance> attendance = attendanceService.getAllAttendance();
        return ResponseEntity.ok(attendance);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRINCIPAL') or hasRole('FACULTY')")
    public ResponseEntity<Attendance> getAttendanceById(@PathVariable Long id) {
        return attendanceService.getAttendanceById(id)
                .map(attendance -> ResponseEntity.ok(attendance))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/student/{studentId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('FACULTY') or @studentService.getStudentById(#studentId).get().userId == authentication.principal.userId")
    public ResponseEntity<List<Attendance>> getAttendanceByStudent(@PathVariable Long studentId) {
        List<Attendance> attendance = attendanceService.getAttendanceByStudent(studentId);
        return ResponseEntity.ok(attendance);
    }

    @GetMapping("/subject/{subjectId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRINCIPAL') or hasRole('FACULTY')")
    public ResponseEntity<List<Attendance>> getAttendanceBySubject(@PathVariable Long subjectId) {
        List<Attendance> attendance = attendanceService.getAttendanceBySubject(subjectId);
        return ResponseEntity.ok(attendance);
    }

    @GetMapping("/date/{date}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRINCIPAL') or hasRole('FACULTY')")
    public ResponseEntity<List<Attendance>> getAttendanceByDate(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<Attendance> attendance = attendanceService.getAttendanceByDate(date);
        return ResponseEntity.ok(attendance);
    }

    @GetMapping("/student/{studentId}/subject/{subjectId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('FACULTY') or @studentService.getStudentById(#studentId).get().userId == authentication.principal.userId")
    public ResponseEntity<List<Attendance>> getAttendanceByStudentAndSubject(
            @PathVariable Long studentId, @PathVariable Long subjectId) {
        List<Attendance> attendance = attendanceService.getAttendanceByStudentAndSubject(studentId, subjectId);
        return ResponseEntity.ok(attendance);
    }

    @GetMapping("/student/{studentId}/daterange")
    @PreAuthorize("hasRole('ADMIN') or hasRole('FACULTY') or @studentService.getStudentById(#studentId).get().userId == authentication.principal.userId")
    public ResponseEntity<List<Attendance>> getAttendanceByDateRange(
            @PathVariable Long studentId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<Attendance> attendance = attendanceService.getAttendanceByDateRange(studentId, startDate, endDate);
        return ResponseEntity.ok(attendance);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRINCIPAL') or hasRole('FACULTY')")
    public ResponseEntity<Attendance> markAttendance(@Valid @RequestBody Attendance attendance) {
        Attendance markedAttendance = attendanceService.markAttendance(attendance);
        return ResponseEntity.status(HttpStatus.CREATED).body(markedAttendance);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRINCIPAL') or hasRole('FACULTY')")
    public ResponseEntity<Attendance> updateAttendance(@PathVariable Long id,
            @Valid @RequestBody Attendance attendanceDetails) {
        try {
            Attendance updatedAttendance = attendanceService.updateAttendance(id, attendanceDetails);
            return ResponseEntity.ok(updatedAttendance);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteAttendance(@PathVariable Long id) {
        try {
            attendanceService.deleteAttendance(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/student/{studentId}/subject/{subjectId}/percentage")
    @PreAuthorize("hasRole('ADMIN') or hasRole('FACULTY') or @studentService.getStudentById(#studentId).get().userId == authentication.principal.userId")
    public ResponseEntity<Double> getAttendancePercentage(@PathVariable Long studentId, @PathVariable Long subjectId) {
        double percentage = attendanceService.getAttendancePercentage(studentId, subjectId);
        return ResponseEntity.ok(percentage);
    }
}
