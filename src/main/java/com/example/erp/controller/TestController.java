package com.example.erp.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class TestController {

    @GetMapping("/")
    public ResponseEntity<Map<String, Object>> root() {
        Map<String, Object> response = new HashMap<>();
        response.put("application", "College ERP Backend");
        response.put("documentation", "/swagger-ui.html");
        response.put("health", "/actuator/health");
        response.put("version", "1.0.0");
        response.put("status", "running");
        response.put("timestamp", java.time.LocalDateTime.now().toString());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/test/public")
    public String publicEndpoint() {
        return "This is a public endpoint";
    }

    @GetMapping("/api/test/authenticated")
    @PreAuthorize("isAuthenticated()")
    public String authenticatedEndpoint() {
        return "This is an authenticated endpoint";
    }
}
