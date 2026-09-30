package com.placement.controller;

import com.placement.dto.ApiResponse;
import com.placement.dto.JobApplicationResponse;
import com.placement.dto.UpdateApplicationStatusRequest;
import com.placement.service.JobApplicationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/applications")
public class JobApplicationController {

    private final JobApplicationService applicationService;

    public JobApplicationController(JobApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @PostMapping("/apply/{driveId}")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<ApiResponse<JobApplicationResponse>> applyForDrive(
            @PathVariable Long driveId,
            @AuthenticationPrincipal UserDetails userDetails) {
        JobApplicationResponse response = applicationService.applyForDrive(driveId, userDetails.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Successfully applied for the placement drive", response));
    }

    @GetMapping("/my-applications")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<ApiResponse<List<JobApplicationResponse>>> getMyApplications(
            @AuthenticationPrincipal UserDetails userDetails) {
        List<JobApplicationResponse> applications = applicationService.getMyApplications(userDetails.getUsername());
        return ResponseEntity.ok(ApiResponse.success(applications));
    }

    @GetMapping("/drive/{driveId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'COMPANY')")
    public ResponseEntity<ApiResponse<List<JobApplicationResponse>>> getApplicationsForDrive(
            @PathVariable Long driveId) {
        List<JobApplicationResponse> applications = applicationService.getApplicationsForDrive(driveId);
        return ResponseEntity.ok(ApiResponse.success(applications));
    }

    @RequestMapping(value = "/{id}/status", method = {RequestMethod.PATCH, RequestMethod.PUT})
    @PreAuthorize("hasAnyRole('ADMIN', 'COMPANY')")
    public ResponseEntity<ApiResponse<JobApplicationResponse>> updateApplicationStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdateApplicationStatusRequest request) {
        JobApplicationResponse updated = applicationService.updateApplicationStatus(id, request);
        return ResponseEntity.ok(ApiResponse.success("Application status updated successfully", updated));
    }
}
