package com.sqlmurdermystery.progress.model;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "activity_log")
public class ActivityLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String username;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ActivityType type;

    @Column(name = "reference_id", nullable = false)
    private Long referenceId;

    @Column(name = "reference_title", length = 150)
    private String referenceTitle;

    @Column(name = "points_awarded", nullable = false)
    private Integer pointsAwarded = 0;

    @Column(name = "occurred_at", nullable = false)
    private Instant occurredAt = Instant.now();

    public ActivityLog() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public ActivityType getType() { return type; }
    public void setType(ActivityType type) { this.type = type; }

    public Long getReferenceId() { return referenceId; }
    public void setReferenceId(Long referenceId) { this.referenceId = referenceId; }

    public String getReferenceTitle() { return referenceTitle; }
    public void setReferenceTitle(String referenceTitle) { this.referenceTitle = referenceTitle; }

    public Integer getPointsAwarded() { return pointsAwarded; }
    public void setPointsAwarded(Integer pointsAwarded) { this.pointsAwarded = pointsAwarded; }

    public Instant getOccurredAt() { return occurredAt; }
    public void setOccurredAt(Instant occurredAt) { this.occurredAt = occurredAt; }
}
