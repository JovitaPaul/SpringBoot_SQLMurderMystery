package com.sqlmurdermystery.progress.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "user_progress")
public class UserProgress {

    @Id
    @Column(nullable = false, length = 50)
    private String username;   // from the JWT — this service does not own the User table

    @Column(name = "total_points", nullable = false)
    private Integer totalPoints = 0;

    @Column(name = "quizzes_passed", nullable = false)
    private Integer quizzesPassed = 0;

    @Column(name = "cases_solved", nullable = false)
    private Integer casesSolved = 0;

    @Column(name = "current_streak", nullable = false)
    private Integer currentStreak = 0;

    @Column(name = "longest_streak", nullable = false)
    private Integer longestStreak = 0;

    @Column(name = "last_activity_date")
    private LocalDate lastActivityDate;

    public UserProgress() {}

    public UserProgress(String username) {
        this.username = username;
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

    public LocalDate getLastActivityDate() { return lastActivityDate; }
    public void setLastActivityDate(LocalDate lastActivityDate) { this.lastActivityDate = lastActivityDate; }
}
