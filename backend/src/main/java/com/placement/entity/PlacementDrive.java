package com.placement.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "placement_drives")
public class PlacementDrive {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @NotBlank
    @Column(nullable = false, length = 150)
    private String title;

    @NotBlank
    @Column(name = "job_role", nullable = false, length = 100)
    private String jobRole;

    @Column(columnDefinition = "TEXT")
    private String description;

    @NotNull
    @Column(name = "package_lpa", nullable = false)
    private Double packageLpa;

    @Column(length = 100)
    private String location;

    @Column(name = "drive_date")
    private LocalDate driveDate;

    @Column(name = "deadline")
    private LocalDateTime deadline;

    // Eligibility criteria
    @Column(name = "min_cgpa")
    private Double minCgpa = 0.0;

    @Column(name = "max_backlogs")
    private Integer maxBacklogs = 0;

    @Column(name = "min_tenth_percentage")
    private Double minTenthPercentage = 0.0;

    @Column(name = "min_twelfth_percentage")
    private Double minTwelfthPercentage = 0.0;

    // Comma-separated list of departments, e.g. "CSE,IT,ECE" or "ALL"
    @Column(name = "eligible_departments", length = 255)
    private String eligibleDepartments = "ALL";

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private DriveStatus status = DriveStatus.UPCOMING;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public PlacementDrive() {}

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        if (this.status == null) this.status = DriveStatus.UPCOMING;
        if (this.minCgpa == null) this.minCgpa = 0.0;
        if (this.maxBacklogs == null) this.maxBacklogs = 0;
        if (this.minTenthPercentage == null) this.minTenthPercentage = 0.0;
        if (this.minTwelfthPercentage == null) this.minTwelfthPercentage = 0.0;
        if (this.eligibleDepartments == null || this.eligibleDepartments.isBlank()) this.eligibleDepartments = "ALL";
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Company getCompany() { return company; }
    public void setCompany(Company company) { this.company = company; }

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

    public DriveStatus getStatus() { return status; }
    public void setStatus(DriveStatus status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}
