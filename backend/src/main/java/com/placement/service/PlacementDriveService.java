package com.placement.service;

import com.placement.dto.EligibilityResponse;
import com.placement.dto.PlacementDriveRequest;
import com.placement.dto.PlacementDriveResponse;
import com.placement.entity.*;
import com.placement.exception.ResourceNotFoundException;
import com.placement.repository.CompanyRepository;
import com.placement.repository.JobApplicationRepository;
import com.placement.repository.PlacementDriveRepository;
import com.placement.repository.StudentProfileRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PlacementDriveService {

    private final PlacementDriveRepository driveRepository;
    private final CompanyRepository companyRepository;
    private final StudentProfileRepository studentProfileRepository;
    private final JobApplicationRepository jobApplicationRepository;

    public PlacementDriveService(PlacementDriveRepository driveRepository,
                                 CompanyRepository companyRepository,
                                 StudentProfileRepository studentProfileRepository,
                                 JobApplicationRepository jobApplicationRepository) {
        this.driveRepository = driveRepository;
        this.companyRepository = companyRepository;
        this.studentProfileRepository = studentProfileRepository;
        this.jobApplicationRepository = jobApplicationRepository;
    }

    @Transactional(readOnly = true)
    public List<PlacementDriveResponse> getAllDrives(String currentUserEmail) {
        StudentProfile student = null;
        if (currentUserEmail != null) {
            student = studentProfileRepository.findByUserEmail(currentUserEmail).orElse(null);
        }

        final StudentProfile finalStudent = student;
        return driveRepository.findAll().stream()
                .map(drive -> mapToResponse(drive, finalStudent))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PlacementDriveResponse getDriveById(Long id, String currentUserEmail) {
        PlacementDrive drive = driveRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Placement drive not found with id: " + id));

        StudentProfile student = null;
        if (currentUserEmail != null) {
            student = studentProfileRepository.findByUserEmail(currentUserEmail).orElse(null);
        }
        return mapToResponse(drive, student);
    }

    @Transactional
    public PlacementDriveResponse createDrive(PlacementDriveRequest request) {
        Company company = companyRepository.findById(request.getCompanyId())
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with id: " + request.getCompanyId()));

        PlacementDrive drive = new PlacementDrive();
        drive.setCompany(company);
        updateDriveFromRequest(drive, request);

        PlacementDrive saved = driveRepository.save(drive);
        return mapToResponse(saved, null);
    }

    @Transactional
    public PlacementDriveResponse updateDrive(Long id, PlacementDriveRequest request) {
        PlacementDrive drive = driveRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Placement drive not found with id: " + id));

        if (!drive.getCompany().getId().equals(request.getCompanyId())) {
            Company company = companyRepository.findById(request.getCompanyId())
                    .orElseThrow(() -> new ResourceNotFoundException("Company not found with id: " + request.getCompanyId()));
            drive.setCompany(company);
        }

        updateDriveFromRequest(drive, request);
        PlacementDrive saved = driveRepository.save(drive);
        return mapToResponse(saved, null);
    }

    @Transactional
    public void deleteDrive(Long id) {
        PlacementDrive drive = driveRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Placement drive not found with id: " + id));
        driveRepository.delete(drive);
    }

    /**
     * Complete eligibility evaluation checking:
     * 1. CGPA >= minCgpa
     * 2. Backlogs <= maxBacklogs
     * 3. 10th % >= minTenthPercentage
     * 4. 12th % >= minTwelfthPercentage
     * 5. Department in eligibleDepartments (or ALL)
     */
    public EligibilityResponse checkEligibility(Long driveId, String studentEmail) {
        PlacementDrive drive = driveRepository.findById(driveId)
                .orElseThrow(() -> new ResourceNotFoundException("Placement drive not found with id: " + driveId));

        StudentProfile student = studentProfileRepository.findByUserEmail(studentEmail)
                .orElse(null);

        if (student == null) {
            return new EligibilityResponse(false, "Please complete your student profile before checking eligibility.", List.of());
        }

        List<EligibilityResponse.CriterionCheck> checks = new ArrayList<>();
        boolean isEligible = true;

        // 1. CGPA check
        boolean cgpaPass = student.getCgpa() >= drive.getMinCgpa();
        if (!cgpaPass) isEligible = false;
        checks.add(new EligibilityResponse.CriterionCheck(
                "Minimum CGPA",
                ">= " + drive.getMinCgpa(),
                student.getCgpa().toString(),
                cgpaPass
        ));

        // 2. Backlog check
        boolean backlogPass = student.getBacklogs() <= drive.getMaxBacklogs();
        if (!backlogPass) isEligible = false;
        checks.add(new EligibilityResponse.CriterionCheck(
                "Maximum Allowed Backlogs",
                "<= " + drive.getMaxBacklogs(),
                student.getBacklogs().toString(),
                backlogPass
        ));

        // 3. 10th Percentage
        boolean tenthPass = student.getTenthPercentage() >= drive.getMinTenthPercentage();
        if (!tenthPass) isEligible = false;
        checks.add(new EligibilityResponse.CriterionCheck(
                "10th Standard Percentage",
                ">= " + drive.getMinTenthPercentage() + "%",
                student.getTenthPercentage() + "%",
                tenthPass
        ));

        // 4. 12th Percentage
        boolean twelfthPass = student.getTwelfthPercentage() >= drive.getMinTwelfthPercentage();
        if (!twelfthPass) isEligible = false;
        checks.add(new EligibilityResponse.CriterionCheck(
                "12th Standard Percentage",
                ">= " + drive.getMinTwelfthPercentage() + "%",
                student.getTwelfthPercentage() + "%",
                twelfthPass
        ));

        // 5. Department check
        boolean deptPass = true;
        String eligibleDepts = drive.getEligibleDepartments();
        if (eligibleDepts != null && !eligibleDepts.equalsIgnoreCase("ALL") && !eligibleDepts.isBlank()) {
            List<String> allowed = Arrays.stream(eligibleDepts.split(","))
                    .map(String::trim)
                    .map(String::toUpperCase)
                    .collect(Collectors.toList());
            deptPass = allowed.contains(student.getDepartment().toUpperCase().trim());
        }
        if (!deptPass) isEligible = false;
        checks.add(new EligibilityResponse.CriterionCheck(
                "Department",
                drive.getEligibleDepartments(),
                student.getDepartment(),
                deptPass
        ));

        String reason = isEligible ? "You meet all eligibility criteria for this placement drive."
                : "You do not meet one or more eligibility criteria for this placement drive.";

        return new EligibilityResponse(isEligible, reason, checks);
    }

    private void updateDriveFromRequest(PlacementDrive drive, PlacementDriveRequest request) {
        drive.setTitle(request.getTitle().trim());
        drive.setJobRole(request.getJobRole().trim());
        drive.setDescription(request.getDescription());
        drive.setPackageLpa(request.getPackageLpa());
        drive.setLocation(request.getLocation());
        drive.setDriveDate(request.getDriveDate());
        drive.setDeadline(request.getDeadline());
        drive.setMinCgpa(request.getMinCgpa() != null ? request.getMinCgpa() : 0.0);
        drive.setMaxBacklogs(request.getMaxBacklogs() != null ? request.getMaxBacklogs() : 0);
        drive.setMinTenthPercentage(request.getMinTenthPercentage() != null ? request.getMinTenthPercentage() : 0.0);
        drive.setMinTwelfthPercentage(request.getMinTwelfthPercentage() != null ? request.getMinTwelfthPercentage() : 0.0);
        drive.setEligibleDepartments(request.getEligibleDepartments() != null ? request.getEligibleDepartments() : "ALL");

        if (request.getStatus() != null) {
            try {
                drive.setStatus(DriveStatus.valueOf(request.getStatus().toUpperCase()));
            } catch (IllegalArgumentException ignored) {}
        }
    }

    public PlacementDriveResponse mapToResponse(PlacementDrive drive, StudentProfile student) {
        PlacementDriveResponse resp = new PlacementDriveResponse();
        resp.setId(drive.getId());
        resp.setCompanyId(drive.getCompany().getId());
        resp.setCompanyName(drive.getCompany().getName());
        resp.setCompanyLogoUrl(drive.getCompany().getLogoUrl());
        resp.setCompanyLocation(drive.getCompany().getLocation());
        resp.setTitle(drive.getTitle());
        resp.setJobRole(drive.getJobRole());
        resp.setDescription(drive.getDescription());
        resp.setPackageLpa(drive.getPackageLpa());
        resp.setLocation(drive.getLocation());
        resp.setDriveDate(drive.getDriveDate());
        resp.setDeadline(drive.getDeadline());
        resp.setMinCgpa(drive.getMinCgpa());
        resp.setMaxBacklogs(drive.getMaxBacklogs());
        resp.setMinTenthPercentage(drive.getMinTenthPercentage());
        resp.setMinTwelfthPercentage(drive.getMinTwelfthPercentage());
        resp.setEligibleDepartments(drive.getEligibleDepartments());
        resp.setStatus(drive.getStatus().name());

        List<JobApplication> applications = jobApplicationRepository.findByDriveId(drive.getId());
        resp.setTotalApplications(applications.size());

        if (student != null) {
            boolean hasApplied = applications.stream()
                    .anyMatch(a -> a.getStudentProfile().getId().equals(student.getId()));
            resp.setHasApplied(hasApplied);

            // Calculate quick eligibility
            boolean eligible = student.getCgpa() >= drive.getMinCgpa() &&
                    student.getBacklogs() <= drive.getMaxBacklogs() &&
                    student.getTenthPercentage() >= drive.getMinTenthPercentage() &&
                    student.getTwelfthPercentage() >= drive.getMinTwelfthPercentage();
            if (eligible && drive.getEligibleDepartments() != null && !drive.getEligibleDepartments().equalsIgnoreCase("ALL")) {
                List<String> allowed = Arrays.stream(drive.getEligibleDepartments().split(","))
                        .map(String::trim)
                        .map(String::toUpperCase)
                        .collect(Collectors.toList());
                eligible = allowed.contains(student.getDepartment().toUpperCase().trim());
            }
            resp.setIsEligibleForCurrentUser(eligible);
        }

        return resp;
    }
}
