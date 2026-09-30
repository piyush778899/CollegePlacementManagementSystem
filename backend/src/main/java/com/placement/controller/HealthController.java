package com.placement.controller;

import com.placement.dto.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class HealthController {

    @GetMapping("/health")
    public ResponseEntity<ApiResponse<Map<String, String>>> healthCheck() {
        Map<String, String> info = Map.of(
            "status", "UP",
            "application", "College Placement Management System",
            "version", "1.0.0"
        );
        return ResponseEntity.ok(ApiResponse.success(info));
    }
}
