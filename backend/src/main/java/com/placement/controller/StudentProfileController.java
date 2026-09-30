package com.placement.controller;

import com.placement.dto.ApiResponse;
import com.placement.dto.StudentProfileRequest;
import com.placement.dto.StudentProfileResponse;
import com.placement.service.StudentProfileService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentProfileController {

    private final StudentProfileService studentProfileService;

    public StudentProfileController(StudentProfileService studentProfileService) {
        this.studentProfileService = studentProfileService;
    }

    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<StudentProfileResponse>> getMyProfile(
            @AuthenticationPrincipal UserDetails userDetails) {
        StudentProfileResponse profile = studentProfileService.getProfileByEmail(userDetails.getUsername());
        return ResponseEntity.ok(ApiResponse.success(profile));
    }

    @PutMapping("/profile")
    public ResponseEntity<ApiResponse<StudentProfileResponse>> updateMyProfile(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody StudentProfileRequest request) {
        StudentProfileResponse updated = studentProfileService.saveOrUpdateProfile(userDetails.getUsername(), request);
        return ResponseEntity.ok(ApiResponse.success("Profile saved successfully", updated));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'COMPANY')")
    public ResponseEntity<ApiResponse<List<StudentProfileResponse>>> getAllProfiles() {
        List<StudentProfileResponse> profiles = studentProfileService.getAllProfiles();
        return ResponseEntity.ok(ApiResponse.success(profiles));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'COMPANY')")
    public ResponseEntity<ApiResponse<StudentProfileResponse>> getProfileById(@PathVariable Long id) {
        StudentProfileResponse profile = studentProfileService.getProfileById(id);
        return ResponseEntity.ok(ApiResponse.success(profile));
    }
}
