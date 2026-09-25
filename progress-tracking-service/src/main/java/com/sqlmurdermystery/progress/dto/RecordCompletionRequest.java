package com.sqlmurdermystery.progress.dto;

import com.sqlmurdermystery.progress.model.ActivityType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class RecordCompletionRequest {

    @NotNull(message = "type is required")
    private ActivityType type;

    @NotNull(message = "referenceId is required")
    private Long referenceId;

    private String referenceTitle;

    @NotNull(message = "points is required")
    @Positive(message = "points must be positive")
    private Integer points;

    public RecordCompletionRequest() {}

    public ActivityType getType() { return type; }
    public void setType(ActivityType type) { this.type = type; }

    public Long getReferenceId() { return referenceId; }
    public void setReferenceId(Long referenceId) { this.referenceId = referenceId; }

    public String getReferenceTitle() { return referenceTitle; }
    public void setReferenceTitle(String referenceTitle) { this.referenceTitle = referenceTitle; }

    public Integer getPoints() { return points; }
    public void setPoints(Integer points) { this.points = points; }
}
