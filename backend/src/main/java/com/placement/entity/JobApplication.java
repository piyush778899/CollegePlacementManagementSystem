package com.placement.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(
    name = "job_applications",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"student_profile_id", "drive_id"})
    }
)
public class JobApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "student_profile_id", nullable = false)
    private StudentProfile studentProfile;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "drive_id", nullable = false)
    private PlacementDrive drive;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private ApplicationStatus status = ApplicationStatus.APPLIED;

    @Column(name = "current_round", nullable = false)
    private Integer currentRound = 1;

    @Column(name = "feedback", columnDefinition = "TEXT")
    private String feedback;

    @Column(name = "applied_at", nullable = false, updatable = false)
    private LocalDateTime appliedAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public JobApplication() {}

    public JobApplication(StudentProfile studentProfile, PlacementDrive drive) {
        this.studentProfile = studentProfile;
        this.drive = drive;
    }

    @PrePersist
    protected void onCreate() {
        this.appliedAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        if (this.status == null) this.status = ApplicationStatus.APPLIED;
        if (this.currentRound == null) this.currentRound = 1;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public StudentProfile getStudentProfile() { return studentProfile; }
    public void setStudentProfile(StudentProfile studentProfile) { this.studentProfile = studentProfile; }

    public PlacementDrive getDrive() { return drive; }
    public void setDrive(PlacementDrive drive) { this.drive = drive; }

    public ApplicationStatus getStatus() { return status; }
    public void setStatus(ApplicationStatus status) { this.status = status; }

    public Integer getCurrentRound() { return currentRound; }
    public void setCurrentRound(Integer currentRound) { this.currentRound = currentRound; }

    public String getFeedback() { return feedback; }
    public void setFeedback(String feedback) { this.feedback = feedback; }

    public LocalDateTime getAppliedAt() { return appliedAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}
