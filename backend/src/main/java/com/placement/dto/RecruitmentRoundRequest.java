package com.placement.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class RecruitmentRoundRequest {

    @NotNull(message = "Round order is required")
    private Integer roundOrder;

    @NotBlank(message = "Round name is required")
    private String roundName;

    private String description;

    private LocalDateTime scheduledAt;

    public RecruitmentRoundRequest() {}

    public Integer getRoundOrder() { return roundOrder; }
    public void setRoundOrder(Integer roundOrder) { this.roundOrder = roundOrder; }

    public String getRoundName() { return roundName; }
    public void setRoundName(String roundName) { this.roundName = roundName; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDateTime getScheduledAt() { return scheduledAt; }
    public void setScheduledAt(LocalDateTime scheduledAt) { this.scheduledAt = scheduledAt; }
}
