package com.placement.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class PlacementDriveRequest {

    @NotNull(message = "Company ID is required")
    private Long companyId;

    @NotBlank(message = "Drive title is required")
    private String title;

    @NotBlank(message = "Job role is required")
    private String jobRole;

    private String description;

    @NotNull(message = "Package (LPA) is required")
    private Double packageLpa;

    private String location;
    private LocalDate driveDate;
    private LocalDateTime deadline;

    private Double minCgpa = 0.0;
    private Integer maxBacklogs = 0;
    private Double minTenthPercentage = 0.0;
    private Double minTwelfthPercentage = 0.0;
    private String eligibleDepartments = "ALL";
    private String status = "UPCOMING";

    public PlacementDriveRequest() {}

    // Getters and Setters
    public Long getCompanyId() { return companyId; }
    public void setCompanyId(Long companyId) { this.companyId = companyId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getJobRole() { return jobRole; }
    public void setJobRole(String jobRole) { this.jobRole = jobRole; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Double getPackageLpa() { return packageLpa; }
    public void setPackageLpa(Double packageLpa) { this.packageLpa = packageLpa; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public LocalDate getDriveDate() { return driveDate; }
    public void setDriveDate(LocalDate driveDate) { this.driveDate = driveDate; }

    public LocalDateTime getDeadline() { return deadline; }
    public void setDeadline(LocalDateTime deadline) { this.deadline = deadline; }

    public Double getMinCgpa() { return minCgpa; }
    public void setMinCgpa(Double minCgpa) { this.minCgpa = minCgpa; }

    public Integer getMaxBacklogs() { return maxBacklogs; }
    public void setMaxBacklogs(Integer maxBacklogs) { this.maxBacklogs = maxBacklogs; }

    public Double getMinTenthPercentage() { return minTenthPercentage; }
    public void setMinTenthPercentage(Double minTenthPercentage) { this.minTenthPercentage = minTenthPercentage; }

    public Double getMinTwelfthPercentage() { return minTwelfthPercentage; }
    public void setMinTwelfthPercentage(Double minTwelfthPercentage) { this.minTwelfthPercentage = minTwelfthPercentage; }

    public String getEligibleDepartments() { return eligibleDepartments; }
    public void setEligibleDepartments(String eligibleDepartments) { this.eligibleDepartments = eligibleDepartments; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
