package com.sqlmurdermystery.leaderboard.dto;

public class LeaderboardEntryDto {
    private int rank;
    private String username;
    private long score;

    public LeaderboardEntryDto() {}

    public LeaderboardEntryDto(int rank, String username, long score) {
        this.rank = rank;
        this.username = username;
        this.score = score;
    }

    public int getRank() { return rank; }
    public void setRank(int rank) { this.rank = rank; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public long getScore() { return score; }
    public void setScore(long score) { this.score = score; }
}
