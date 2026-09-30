package com.placement.dto;

import java.time.LocalDateTime;

public class RecruitmentRoundResponse {

    private Long id;
    private Long driveId;
    private Integer roundOrder;
    private String roundName;
    private String description;
    private LocalDateTime scheduledAt;

    public RecruitmentRoundResponse() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getDriveId() { return driveId; }
    public void setDriveId(Long driveId) { this.driveId = driveId; }

    public Integer getRoundOrder() { return roundOrder; }
    public void setRoundOrder(Integer roundOrder) { this.roundOrder = roundOrder; }

    public String getRoundName() { return roundName; }
    public void setRoundName(String roundName) { this.roundName = roundName; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDateTime getScheduledAt() { return scheduledAt; }
    public void setScheduledAt(LocalDateTime scheduledAt) { this.scheduledAt = scheduledAt; }
}
