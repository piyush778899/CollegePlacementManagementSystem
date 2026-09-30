package com.placement.controller;

import com.placement.dto.ApiResponse;
import com.placement.dto.RecruitmentRoundRequest;
import com.placement.dto.RecruitmentRoundResponse;
import com.placement.service.RecruitmentRoundService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class RecruitmentRoundController {

    private final RecruitmentRoundService roundService;

    public RecruitmentRoundController(RecruitmentRoundService roundService) {
        this.roundService = roundService;
    }

    @GetMapping("/drives/{driveId}/rounds")
    public ResponseEntity<ApiResponse<List<RecruitmentRoundResponse>>> getRoundsForDrive(
            @PathVariable Long driveId) {
        List<RecruitmentRoundResponse> rounds = roundService.getRoundsByDriveId(driveId);
        return ResponseEntity.ok(ApiResponse.success(rounds));
    }

    @PostMapping("/drives/{driveId}/rounds")
    @PreAuthorize("hasAnyRole('ADMIN', 'COMPANY')")
    public ResponseEntity<ApiResponse<RecruitmentRoundResponse>> addRound(
            @PathVariable Long driveId,
            @Valid @RequestBody RecruitmentRoundRequest request) {
        RecruitmentRoundResponse created = roundService.createRound(driveId, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Recruitment round added successfully", created));
    }

    @DeleteMapping("/rounds/{roundId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'COMPANY')")
    public ResponseEntity<ApiResponse<Void>> deleteRound(@PathVariable Long roundId) {
        roundService.deleteRound(roundId);
        return ResponseEntity.ok(ApiResponse.success("Recruitment round deleted successfully", null));
    }
}
