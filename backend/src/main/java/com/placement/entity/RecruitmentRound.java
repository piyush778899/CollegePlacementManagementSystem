package com.placement.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "recruitment_rounds")
public class RecruitmentRound {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "drive_id", nullable = false)
    private PlacementDrive drive;

    @NotNull
    @Column(name = "round_order", nullable = false)
    private Integer roundOrder;

    @NotBlank
    @Column(name = "round_name", nullable = false, length = 100)
    private String roundName; // e.g. "Online Assessment", "Technical Interview 1", "HR Interview"

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "scheduled_at")
    private LocalDateTime scheduledAt;

    public RecruitmentRound() {}

    public RecruitmentRound(PlacementDrive drive, Integer roundOrder, String roundName, String description, LocalDateTime scheduledAt) {
        this.drive = drive;
        this.roundOrder = roundOrder;
        this.roundName = roundName;
        this.description = description;
        this.scheduledAt = scheduledAt;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public PlacementDrive getDrive() { return drive; }
    public void setDrive(PlacementDrive drive) { this.drive = drive; }

    public Integer getRoundOrder() { return roundOrder; }
    public void setRoundOrder(Integer roundOrder) { this.roundOrder = roundOrder; }

    public String getRoundName() { return roundName; }
    public void setRoundName(String roundName) { this.roundName = roundName; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDateTime getScheduledAt() { return scheduledAt; }
    public void setScheduledAt(LocalDateTime scheduledAt) { this.scheduledAt = scheduledAt; }
}
