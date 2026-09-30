package com.placement.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class PlacementDriveResponse {

    private Long id;
    private Long companyId;
    private String companyName;
    private String companyLogoUrl;
    private String companyLocation;
    private String title;
    private String jobRole;
    private String description;
    private Double packageLpa;
    private String location;
    private LocalDate driveDate;
    private LocalDateTime deadline;
    private Double minCgpa;
    private Integer maxBacklogs;
    private Double minTenthPercentage;
    private Double minTwelfthPercentage;
    private String eligibleDepartments;
    private String status;
    private Integer totalApplications;
    private Boolean isEligibleForCurrentUser;
    private Boolean hasApplied;

    public PlacementDriveResponse() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getCompanyId() { return companyId; }
    public void setCompanyId(Long companyId) { this.companyId = companyId; }

    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }

    public String getCompanyLogoUrl() { return companyLogoUrl; }
    public void setCompanyLogoUrl(String companyLogoUrl) { this.companyLogoUrl = companyLogoUrl; }

    public String getCompanyLocation() { return companyLocation; }
    public void setCompanyLocation(String companyLocation) { this.companyLocation = companyLocation; }

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

    public Integer getTotalApplications() { return totalApplications; }
    public void setTotalApplications(Integer totalApplications) { this.totalApplications = totalApplications; }

    public Boolean getIsEligibleForCurrentUser() { return isEligibleForCurrentUser; }
    public void setIsEligibleForCurrentUser(Boolean eligible) { isEligibleForCurrentUser = eligible; }

    public Boolean getHasApplied() { return hasApplied; }
    public void setHasApplied(Boolean hasApplied) { this.hasApplied = hasApplied; }
}
