package com.sqlmurdermystery.progress.dto;

import java.time.LocalDate;
import java.util.List;

public class ProgressSummaryDto {
    private String username;
    private Integer totalPoints;
    private Integer quizzesPassed;
    private Integer casesSolved;
    private Integer currentStreak;
    private Integer longestStreak;
    private LocalDate lastActivityDate;
    private List<Long> solvedCaseIds;
    private List<Long> passedQuizIds;
    private List<ActivityDto> recentActivity;

    public ProgressSummaryDto() {}

    public ProgressSummaryDto(String username, Integer totalPoints, Integer quizzesPassed, Integer casesSolved,
                               Integer currentStreak, Integer longestStreak, LocalDate lastActivityDate,
                               List<Long> solvedCaseIds, List<Long> passedQuizIds,
                               List<ActivityDto> recentActivity) {
        this.username = username;
        this.totalPoints = totalPoints;
        this.quizzesPassed = quizzesPassed;
        this.casesSolved = casesSolved;
        this.currentStreak = currentStreak;
        this.longestStreak = longestStreak;
        this.lastActivityDate = lastActivityDate;
        this.solvedCaseIds = solvedCaseIds;
        this.passedQuizIds = passedQuizIds;
        this.recentActivity = recentActivity;
    }

    public String getUsername() { return username; }
    public Integer getTotalPoints() { return totalPoints; }
    public Integer getQuizzesPassed() { return quizzesPassed; }
    public Integer getCasesSolved() { return casesSolved; }
    public Integer getCurrentStreak() { return currentStreak; }
    public Integer getLongestStreak() { return longestStreak; }
    public LocalDate getLastActivityDate() { return lastActivityDate; }
    public List<Long> getSolvedCaseIds() { return solvedCaseIds; }
    public List<Long> getPassedQuizIds() { return passedQuizIds; }
    public List<ActivityDto> getRecentActivity() { return recentActivity; }

    public void setUsername(String username) { this.username = username; }
    public void setTotalPoints(Integer totalPoints) { this.totalPoints = totalPoints; }
    public void setQuizzesPassed(Integer quizzesPassed) { this.quizzesPassed = quizzesPassed; }
    public void setCasesSolved(Integer casesSolved) { this.casesSolved = casesSolved; }
    public void setCurrentStreak(Integer currentStreak) { this.currentStreak = currentStreak; }
    public void setLongestStreak(Integer longestStreak) { this.longestStreak = longestStreak; }
    public void setLastActivityDate(LocalDate lastActivityDate) { this.lastActivityDate = lastActivityDate; }
    public void setSolvedCaseIds(List<Long> solvedCaseIds) { this.solvedCaseIds = solvedCaseIds; }
    public void setPassedQuizIds(List<Long> passedQuizIds) { this.passedQuizIds = passedQuizIds; }
    public void setRecentActivity(List<ActivityDto> recentActivity) { this.recentActivity = recentActivity; }
}
