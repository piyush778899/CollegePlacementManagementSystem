package com.placement.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class StudentProfileRequest {

    @NotBlank(message = "Roll number is required")
    private String rollNumber;

    @NotBlank(message = "Department is required")
    private String department;

    @NotNull(message = "CGPA is required")
    @Min(value = 0, message = "CGPA cannot be negative")
    @Max(value = 10, message = "CGPA cannot exceed 10")
    private Double cgpa;

    @NotNull(message = "10th percentage is required")
    @Min(0) @Max(100)
    private Double tenthPercentage;

    @NotNull(message = "12th percentage is required")
    @Min(0) @Max(100)
    private Double twelfthPercentage;

    @NotNull(message = "Backlog count is required")
    @Min(0)
    private Integer backlogs = 0;

    private Integer passingYear;
    private String phone;
    private String gender;
    private String skills;
    private String resumeUrl;

    public StudentProfileRequest() {}

    // Getters & Setters
    public String getRollNumber() { return rollNumber; }
    public void setRollNumber(String rollNumber) { this.rollNumber = rollNumber; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public Double getCgpa() { return cgpa; }
    public void setCgpa(Double cgpa) { this.cgpa = cgpa; }

    public Double getTenthPercentage() { return tenthPercentage; }
    public void setTenthPercentage(Double tenthPercentage) { this.tenthPercentage = tenthPercentage; }

    public Double getTwelfthPercentage() { return twelfthPercentage; }
    public void setTwelfthPercentage(Double twelfthPercentage) { this.twelfthPercentage = twelfthPercentage; }

    public Integer getBacklogs() { return backlogs; }
    public void setBacklogs(Integer backlogs) { this.backlogs = backlogs; }

    public Integer getPassingYear() { return passingYear; }
    public void setPassingYear(Integer passingYear) { this.passingYear = passingYear; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getSkills() { return skills; }
    public void setSkills(String skills) { this.skills = skills; }

    public String getResumeUrl() { return resumeUrl; }
    public void setResumeUrl(String resumeUrl) { this.resumeUrl = resumeUrl; }
}
