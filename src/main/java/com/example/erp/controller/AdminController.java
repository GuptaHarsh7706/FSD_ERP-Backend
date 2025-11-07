package com.example.erp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AdminController {

    private final JdbcTemplate jdbcTemplate;

    @PostMapping("/reset-database")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> resetDatabase() {
        try {
            jdbcTemplate.execute("DELETE FROM log");
            jdbcTemplate.execute("DELETE FROM marks");
            jdbcTemplate.execute("DELETE FROM attendance");
            jdbcTemplate.execute("DELETE FROM enrollment");
            jdbcTemplate.execute("DELETE FROM homework");
            jdbcTemplate.execute("DELETE FROM fees");
            jdbcTemplate.execute("DELETE FROM grade_calculation");
            jdbcTemplate.execute("DELETE FROM syllabus");
            jdbcTemplate.execute("DELETE FROM timetable");
            jdbcTemplate.execute("DELETE FROM exam");
            jdbcTemplate.execute("DELETE FROM subject");
            jdbcTemplate.execute("DELETE FROM student");
            jdbcTemplate.execute("DELETE FROM faculty");
            jdbcTemplate.execute("DELETE FROM department");
            jdbcTemplate.execute("DELETE FROM users");
            jdbcTemplate.execute("DELETE FROM backup");
            jdbcTemplate.execute("DELETE FROM accreditation");
            jdbcTemplate.execute("DELETE FROM academic_calendar");

            jdbcTemplate.execute("ALTER SEQUENCE users_user_id_seq RESTART WITH 1");
            jdbcTemplate.execute("ALTER SEQUENCE student_id_seq RESTART WITH 1");
            jdbcTemplate.execute("ALTER SEQUENCE faculty_id_seq RESTART WITH 1");
            jdbcTemplate.execute("ALTER SEQUENCE department_id_seq RESTART WITH 1");
            jdbcTemplate.execute("ALTER SEQUENCE subject_id_seq RESTART WITH 1");
            jdbcTemplate.execute("ALTER SEQUENCE attendance_id_seq RESTART WITH 1");
            jdbcTemplate.execute("ALTER SEQUENCE marks_id_seq RESTART WITH 1");
            jdbcTemplate.execute("ALTER SEQUENCE exam_id_seq RESTART WITH 1");
            jdbcTemplate.execute("ALTER SEQUENCE fees_id_seq RESTART WITH 1");
            jdbcTemplate.execute("ALTER SEQUENCE enrollment_id_seq RESTART WITH 1");
            jdbcTemplate.execute("ALTER SEQUENCE homework_id_seq RESTART WITH 1");
            jdbcTemplate.execute("ALTER SEQUENCE timetable_id_seq RESTART WITH 1");
            jdbcTemplate.execute("ALTER SEQUENCE syllabus_id_seq RESTART WITH 1");
            jdbcTemplate.execute("ALTER SEQUENCE log_id_seq RESTART WITH 1");
            jdbcTemplate.execute("ALTER SEQUENCE backup_id_seq RESTART WITH 1");
            jdbcTemplate.execute("ALTER SEQUENCE accreditation_id_seq RESTART WITH 1");
            jdbcTemplate.execute("ALTER SEQUENCE academic_calendar_id_seq RESTART WITH 1");
            jdbcTemplate.execute("ALTER SEQUENCE grade_calculation_id_seq RESTART WITH 1");

            Map<String, String> response = new HashMap<>();
            response.put("message", "Database reset successfully. All data deleted and sequences reset to 1.");
            response.put("status", "success");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Error resetting database: " + e.getMessage());
            response.put("status", "error");
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @PostMapping("/reset-users-only")
    public ResponseEntity<Map<String, String>> resetUsersOnly() {
        try {
            jdbcTemplate.execute("DELETE FROM log WHERE user_id IS NOT NULL");
            jdbcTemplate.execute("DELETE FROM student");
            jdbcTemplate.execute("DELETE FROM faculty");
            jdbcTemplate.execute("DELETE FROM users");

            jdbcTemplate.execute("ALTER SEQUENCE users_user_id_seq RESTART WITH 1");
            jdbcTemplate.execute("ALTER SEQUENCE student_id_seq RESTART WITH 1");
            jdbcTemplate.execute("ALTER SEQUENCE faculty_id_seq RESTART WITH 1");

            Map<String, String> response = new HashMap<>();
            response.put("message", "Users reset successfully. User ID will start from 1.");
            response.put("status", "success");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Error resetting users: " + e.getMessage());
            response.put("status", "error");
            return ResponseEntity.internalServerError().body(response);
        }
    }
}
