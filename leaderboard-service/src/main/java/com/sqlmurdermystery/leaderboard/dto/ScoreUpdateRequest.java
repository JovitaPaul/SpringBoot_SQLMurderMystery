package com.sqlmurdermystery.leaderboard.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public class ScoreUpdateRequest {

    @NotNull(message = "totalPoints is required")
    @PositiveOrZero(message = "totalPoints must not be negative")
    private Integer totalPoints;

    public ScoreUpdateRequest() {}

    public Integer getTotalPoints() { return totalPoints; }
    public void setTotalPoints(Integer totalPoints) { this.totalPoints = totalPoints; }
}