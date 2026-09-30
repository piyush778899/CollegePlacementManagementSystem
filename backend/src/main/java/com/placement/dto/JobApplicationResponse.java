package com.placement.dto;

import java.time.LocalDateTime;

public class JobApplicationResponse {

    private Long id;
    private Long driveId;
    private String driveTitle;
    private String companyName;
    private String companyLogoUrl;
    private String jobRole;
    private Double packageLpa;
    private Long studentProfileId;
    private String studentName;
    private String studentEmail;
    private String rollNumber;
    private String department;
    private Double cgpa;
    private Integer backlogs;
    private String resumeUrl;
    private String status;
    private Integer currentRound;
    private String feedback;
    private LocalDateTime appliedAt;

    public JobApplicationResponse() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getDriveId() { return driveId; }
    public void setDriveId(Long driveId) { this.driveId = driveId; }

    public String getDriveTitle() { return driveTitle; }
    public void setDriveTitle(String driveTitle) { this.driveTitle = driveTitle; }

    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }

    public String getCompanyLogoUrl() { return companyLogoUrl; }
    public void setCompanyLogoUrl(String companyLogoUrl) { this.companyLogoUrl = companyLogoUrl; }

    public String getJobRole() { return jobRole; }
    public void setJobRole(String jobRole) { this.jobRole = jobRole; }

    public Double getPackageLpa() { return packageLpa; }
    public void setPackageLpa(Double packageLpa) { this.packageLpa = packageLpa; }

    public Long getStudentProfileId() { return studentProfileId; }
    public void setStudentProfileId(Long studentProfileId) { this.studentProfileId = studentProfileId; }

    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }

    public String getStudentEmail() { return studentEmail; }
    public void setStudentEmail(String studentEmail) { this.studentEmail = studentEmail; }

    public String getRollNumber() { return rollNumber; }
    public void setRollNumber(String rollNumber) { this.rollNumber = rollNumber; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public Double getCgpa() { return cgpa; }
    public void setCgpa(Double cgpa) { this.cgpa = cgpa; }

    public Integer getBacklogs() { return backlogs; }
    public void setBacklogs(Integer backlogs) { this.backlogs = backlogs; }

    public String getResumeUrl() { return resumeUrl; }
    public void setResumeUrl(String resumeUrl) { this.resumeUrl = resumeUrl; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Integer getCurrentRound() { return currentRound; }
    public void setCurrentRound(Integer currentRound) { this.currentRound = currentRound; }

    public String getFeedback() { return feedback; }
    public void setFeedback(String feedback) { this.feedback = feedback; }

    public LocalDateTime getAppliedAt() { return appliedAt; }
    public void setAppliedAt(LocalDateTime appliedAt) { this.appliedAt = appliedAt; }
}
