package com.sqlmurdermystery.progress.dto;

import java.util.List;

public class ProgressSummaryDto {
    private String username;
    private Integer totalPoints;
    private Integer quizzesPassed;
    private Integer casesSolved;
    private Integer currentStreak;
    private Integer longestStreak;
    private List<ActivityDto> recentActivity;

    public ProgressSummaryDto() {}

    public ProgressSummaryDto(String username, Integer totalPoints, Integer quizzesPassed, Integer casesSolved,
                               Integer currentStreak, Integer longestStreak, List<ActivityDto> recentActivity) {
        this.username = username;
        this.totalPoints = totalPoints;
        this.quizzesPassed = quizzesPassed;
        this.casesSolved = casesSolved;
        this.currentStreak = currentStreak;
        this.longestStreak = longestStreak;
        this.recentActivity = recentActivity;
    }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public Integer getTotalPoints() { return totalPoints; }
    public void setTotalPoints(Integer totalPoints) { this.totalPoints = totalPoints; }

    public Integer getQuizzesPassed() { return quizzesPassed; }
    public void setQuizzesPassed(Integer quizzesPassed) { this.quizzesPassed = quizzesPassed; }

    public Integer getCasesSolved() { return casesSolved; }
    public void setCasesSolved(Integer casesSolved) { this.casesSolved = casesSolved; }

    public Integer getCurrentStreak() { return currentStreak; }
    public void setCurrentStreak(Integer currentStreak) { this.currentStreak = currentStreak; }

    public Integer getLongestStreak() { return longestStreak; }
    public void setLongestStreak(Integer longestStreak) { this.longestStreak = longestStreak; }

    public List<ActivityDto> getRecentActivity() { return recentActivity; }
    public void setRecentActivity(List<ActivityDto> recentActivity) { this.recentActivity = recentActivity; }
}
