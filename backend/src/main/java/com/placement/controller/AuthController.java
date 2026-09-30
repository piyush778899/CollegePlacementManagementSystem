package com.placement.controller;

import com.placement.dto.ApiResponse;
import com.placement.dto.AuthResponse;
import com.placement.dto.LoginRequest;
import com.placement.dto.RegisterRequest;
import com.placement.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * POST /api/auth/register — Register a new user.
     */
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponse>> register(
            @Valid @RequestBody RegisterRequest request) {
        AuthResponse authResponse = authService.register(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Registration successful", authResponse));
    }

    /**
     * POST /api/auth/login — Authenticate user and return JWT.
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(
            @Valid @RequestBody LoginRequest request) {
        AuthResponse authResponse = authService.login(request);
        return ResponseEntity.ok(ApiResponse.success("Login successful", authResponse));
    }

    /**
     * GET /api/auth/me — Get current authenticated user info.
     */
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<AuthResponse>> getCurrentUser(
            @AuthenticationPrincipal UserDetails userDetails) {
        AuthResponse authResponse = authService.getCurrentUser(userDetails.getUsername());
        return ResponseEntity.ok(ApiResponse.success(authResponse));
    }
}
