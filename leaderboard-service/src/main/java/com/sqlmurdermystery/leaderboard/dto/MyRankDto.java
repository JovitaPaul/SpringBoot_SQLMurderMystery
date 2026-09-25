package com.sqlmurdermystery.leaderboard.dto;

public class MyRankDto {
    private String username;
    private long score;
    private Integer rank;   // null if the learner has no score yet

    public MyRankDto() {}

    public MyRankDto(String username, long score, Integer rank) {
        this.username = username;
        this.score = score;
        this.rank = rank;
    }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public long getScore() { return score; }
    public void setScore(long score) { this.score = score; }

    public Integer getRank() { return rank; }
    public void setRank(Integer rank) { this.rank = rank; }
}
