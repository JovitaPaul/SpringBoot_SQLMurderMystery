package com.sqlmurdermystery.leaderboard.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class ScoreUpdateRequest {

    @NotNull(message = "points is required")
    @Positive(message = "points must be positive")
    private Integer points;

    public ScoreUpdateRequest() {}

    public Integer getPoints() { return points; }
    public void setPoints(Integer points) { this.points = points; }
}
