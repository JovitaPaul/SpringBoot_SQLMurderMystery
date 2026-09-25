package com.sqlmurdermystery.progress.dto;

import java.time.Instant;

public class ActivityDto {
    private String type;
    private String referenceTitle;
    private Integer pointsAwarded;
    private Instant occurredAt;

    public ActivityDto() {}

    public ActivityDto(String type, String referenceTitle, Integer pointsAwarded, Instant occurredAt) {
        this.type = type;
        this.referenceTitle = referenceTitle;
        this.pointsAwarded = pointsAwarded;
        this.occurredAt = occurredAt;
    }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getReferenceTitle() { return referenceTitle; }
    public void setReferenceTitle(String referenceTitle) { this.referenceTitle = referenceTitle; }

    public Integer getPointsAwarded() { return pointsAwarded; }
    public void setPointsAwarded(Integer pointsAwarded) { this.pointsAwarded = pointsAwarded; }

    public Instant getOccurredAt() { return occurredAt; }
    public void setOccurredAt(Instant occurredAt) { this.occurredAt = occurredAt; }
}
