package com.placement.service;

import com.placement.dto.EligibilityResponse;
import com.placement.dto.JobApplicationResponse;
import com.placement.dto.UpdateApplicationStatusRequest;
import com.placement.entity.*;
import com.placement.exception.ResourceNotFoundException;
import com.placement.repository.JobApplicationRepository;
import com.placement.repository.PlacementDriveRepository;
import com.placement.repository.StudentProfileRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class JobApplicationService {

    private final JobApplicationRepository applicationRepository;
    private final PlacementDriveRepository driveRepository;
    private final StudentProfileRepository profileRepository;
    private final PlacementDriveService placementDriveService;

    public JobApplicationService(JobApplicationRepository applicationRepository,
                                 PlacementDriveRepository driveRepository,
                                 StudentProfileRepository profileRepository,
                                 PlacementDriveService placementDriveService) {
        this.applicationRepository = applicationRepository;
        this.driveRepository = driveRepository;
        this.profileRepository = profileRepository;
        this.placementDriveService = placementDriveService;
    }

    /**
     * Student applies for a placement drive with automated eligibility validation.
     */
    @Transactional
    public JobApplicationResponse applyForDrive(Long driveId, String studentEmail) {
        StudentProfile student = profileRepository.findByUserEmail(studentEmail)
                .orElseThrow(() -> new IllegalArgumentException("Please complete your student profile before applying."));

        PlacementDrive drive = driveRepository.findById(driveId)
                .orElseThrow(() -> new ResourceNotFoundException("Placement drive not found with id: " + driveId));

        if (applicationRepository.existsByStudentProfileIdAndDriveId(student.getId(), drive.getId())) {
            throw new IllegalArgumentException("You have already applied for this placement drive.");
        }

        // Verify eligibility before accepting application
        EligibilityResponse eligibility = placementDriveService.checkEligibility(driveId, studentEmail);
        if (!eligibility.isEligible()) {
            throw new IllegalArgumentException("You are not eligible for this drive: " + eligibility.getReason());
        }

        JobApplication application = new JobApplication(student, drive);
        application.setStatus(ApplicationStatus.APPLIED);
        application.setCurrentRound(1);

        JobApplication saved = applicationRepository.save(application);
        return mapToResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<JobApplicationResponse> getMyApplications(String studentEmail) {
        return applicationRepository.findByStudentProfileUserEmail(studentEmail).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<JobApplicationResponse> getApplicationsForDrive(Long driveId) {
        return applicationRepository.findByDriveId(driveId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public JobApplicationResponse updateApplicationStatus(Long applicationId, UpdateApplicationStatusRequest request) {
        JobApplication application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found with id: " + applicationId));

        try {
            ApplicationStatus newStatus = ApplicationStatus.valueOf(request.getStatus().toUpperCase());
            application.setStatus(newStatus);

            // If candidate is SELECTED, automatically mark student as placed!
            if (newStatus == ApplicationStatus.SELECTED) {
                StudentProfile student = application.getStudentProfile();
                student.setIsPlaced(true);
                profileRepository.save(student);
            }
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid application status: " + request.getStatus());
        }

        if (request.getCurrentRound() != null) {
            application.setCurrentRound(request.getCurrentRound());
        }
        if (request.getFeedback() != null) {
            application.setFeedback(request.getFeedback());
        }

        JobApplication saved = applicationRepository.save(application);
        return mapToResponse(saved);
    }

    public JobApplicationResponse mapToResponse(JobApplication app) {
        JobApplicationResponse resp = new JobApplicationResponse();
        resp.setId(app.getId());
        resp.setDriveId(app.getDrive().getId());
        resp.setDriveTitle(app.getDrive().getTitle());
        resp.setCompanyName(app.getDrive().getCompany().getName());
        resp.setCompanyLogoUrl(app.getDrive().getCompany().getLogoUrl());
        resp.setJobRole(app.getDrive().getJobRole());
        resp.setPackageLpa(app.getDrive().getPackageLpa());

        StudentProfile s = app.getStudentProfile();
        resp.setStudentProfileId(s.getId());
        resp.setStudentName(s.getUser().getFullName());
        resp.setStudentEmail(s.getUser().getEmail());
        resp.setRollNumber(s.getRollNumber());
        resp.setDepartment(s.getDepartment());
        resp.setCgpa(s.getCgpa());
        resp.setBacklogs(s.getBacklogs());
        resp.setResumeUrl(s.getResumeUrl());

        resp.setStatus(app.getStatus().name());
        resp.setCurrentRound(app.getCurrentRound());
        resp.setFeedback(app.getFeedback());
        resp.setAppliedAt(app.getAppliedAt());
        return resp;
    }
}
