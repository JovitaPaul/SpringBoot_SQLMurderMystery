package com.sqlmurdermystery.casecontent.dto;

/** Case list item — deliberately excludes targetSchema and any solution fields. */
public class CaseSummaryDto {
    private Long id;
    private String title;
    private String briefingPreview;
    private String difficulty;
    private Integer pointsReward;

    public CaseSummaryDto() {}

    public CaseSummaryDto(Long id, String title, String briefingPreview, String difficulty, Integer pointsReward) {
        this.id = id;
        this.title = title;
        this.briefingPreview = briefingPreview;
        this.difficulty = difficulty;
        this.pointsReward = pointsReward;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getBriefingPreview() { return briefingPreview; }
    public void setBriefingPreview(String briefingPreview) { this.briefingPreview = briefingPreview; }

    public String getDifficulty() { return difficulty; }
    public void setDifficulty(String difficulty) { this.difficulty = difficulty; }

    public Integer getPointsReward() { return pointsReward; }
    public void setPointsReward(Integer pointsReward) { this.pointsReward = pointsReward; }
}
