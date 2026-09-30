package com.placement.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "student_profiles")
public class StudentProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @NotBlank
    @Column(name = "roll_number", nullable = false, unique = true, length = 50)
    private String rollNumber;

    @NotBlank
    @Column(nullable = false, length = 100)
    private String department; // e.g. CSE, IT, ECE, MECH, CIVIL, etc.

    @NotNull
    @Min(0)
    @Max(10)
    @Column(nullable = false)
    private Double cgpa;

    @NotNull
    @Min(0)
    @Max(100)
    @Column(name = "tenth_percentage", nullable = false)
    private Double tenthPercentage;

    @NotNull
    @Min(0)
    @Max(100)
    @Column(name = "twelfth_percentage", nullable = false)
    private Double twelfthPercentage;

    @NotNull
    @Min(0)
    @Column(nullable = false)
    private Integer backlogs = 0;

    @Column(name = "passing_year")
    private Integer passingYear;

    @Column(length = 30)
    private String phone;

    @Column(length = 20)
    private String gender;

    @Column(columnDefinition = "TEXT")
    private String skills; // comma separated, e.g. Java, Python, React, SQL

    @Column(name = "resume_url", length = 500)
    private String resumeUrl;

    @Column(name = "is_placed", nullable = false)
    private Boolean isPlaced = false;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public StudentProfile() {}

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        if (this.backlogs == null) this.backlogs = 0;
        if (this.isPlaced == null) this.isPlaced = false;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

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

    public Boolean getIsPlaced() { return isPlaced; }
    public void setIsPlaced(Boolean placed) { isPlaced = placed; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}
