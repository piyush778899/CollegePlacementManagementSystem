package com.placement.dto;

import jakarta.validation.constraints.NotBlank;

public class UpdateApplicationStatusRequest {

    @NotBlank(message = "Status is required")
    private String status; // APPLIED, UNDER_REVIEW, SHORTLISTED, IN_INTERVIEW, SELECTED, REJECTED

    private Integer currentRound;
    private String feedback;

    public UpdateApplicationStatusRequest() {}

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Integer getCurrentRound() { return currentRound; }
    public void setCurrentRound(Integer currentRound) { this.currentRound = currentRound; }

    public String getFeedback() { return feedback; }
    public void setFeedback(String feedback) { this.feedback = feedback; }
}
