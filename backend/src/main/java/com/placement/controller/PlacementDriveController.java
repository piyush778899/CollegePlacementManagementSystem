package com.placement.controller;

import com.placement.dto.ApiResponse;
import com.placement.dto.EligibilityResponse;
import com.placement.dto.PlacementDriveRequest;
import com.placement.dto.PlacementDriveResponse;
import com.placement.service.PlacementDriveService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/drives")
public class PlacementDriveController {

    private final PlacementDriveService driveService;

    public PlacementDriveController(PlacementDriveService driveService) {
        this.driveService = driveService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<PlacementDriveResponse>>> getAllDrives(
            @AuthenticationPrincipal UserDetails userDetails) {
        String email = (userDetails != null) ? userDetails.getUsername() : null;
        List<PlacementDriveResponse> drives = driveService.getAllDrives(email);
        return ResponseEntity.ok(ApiResponse.success(drives));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PlacementDriveResponse>> getDriveById(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        String email = (userDetails != null) ? userDetails.getUsername() : null;
        PlacementDriveResponse drive = driveService.getDriveById(id, email);
        return ResponseEntity.ok(ApiResponse.success(drive));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'COMPANY')")
    public ResponseEntity<ApiResponse<PlacementDriveResponse>> createDrive(
            @Valid @RequestBody PlacementDriveRequest request) {
        PlacementDriveResponse created = driveService.createDrive(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Placement drive created successfully", created));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'COMPANY')")
    public ResponseEntity<ApiResponse<PlacementDriveResponse>> updateDrive(
            @PathVariable Long id,
            @Valid @RequestBody PlacementDriveRequest request) {
        PlacementDriveResponse updated = driveService.updateDrive(id, request);
        return ResponseEntity.ok(ApiResponse.success("Placement drive updated successfully", updated));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteDrive(@PathVariable Long id) {
        driveService.deleteDrive(id);
        return ResponseEntity.ok(ApiResponse.success("Placement drive deleted successfully", null));
    }

    @GetMapping("/{id}/eligibility")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<ApiResponse<EligibilityResponse>> checkEligibility(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        EligibilityResponse response = driveService.checkEligibility(id, userDetails.getUsername());
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
