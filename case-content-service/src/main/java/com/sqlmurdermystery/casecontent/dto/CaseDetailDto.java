package com.sqlmurdermystery.casecontent.dto;

/**
 * Full case detail for the Case-Solving Phase editor. Includes targetSchema so the
 * frontend knows which schema to pass to query-execution-service — but never the
 * solution.
 */
public class CaseDetailDto {
    private Long id;
    private String title;
    private String briefing;
    private String difficulty;
    private String targetSchema;
    private Integer pointsReward;

    public CaseDetailDto() {}

    public CaseDetailDto(Long id, String title, String briefing, String difficulty,
                          String targetSchema, Integer pointsReward) {
        this.id = id;
        this.title = title;
        this.briefing = briefing;
        this.difficulty = difficulty;
        this.targetSchema = targetSchema;
        this.pointsReward = pointsReward;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getBriefing() { return briefing; }
    public void setBriefing(String briefing) { this.briefing = briefing; }

    public String getDifficulty() { return difficulty; }
    public void setDifficulty(String difficulty) { this.difficulty = difficulty; }

    public String getTargetSchema() { return targetSchema; }
    public void setTargetSchema(String targetSchema) { this.targetSchema = targetSchema; }

    public Integer getPointsReward() { return pointsReward; }
    public void setPointsReward(Integer pointsReward) { this.pointsReward = pointsReward; }
}
