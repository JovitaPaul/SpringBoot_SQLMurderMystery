package com.sqlmurdermystery.casecontent.model;

import jakarta.persistence.*;

/**
 * Metadata for a Case-Solving Phase mystery.
 *
 * The explorable data (crime scene reports, witnesses, security logs, etc.) does NOT
 * live in this service's database — it lives in its own dedicated MySQL schema
 * ({@link #targetSchema}) that query-execution-service connects to with a read-only
 * account. That keeps whatever a learner can SELECT completely separate from the
 * application's own tables, and keeps the solution out of reach of "SELECT *".
 */
@Entity
@Table(name = "case_files")
public class CaseFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String title;

    @Lob
    @Column(nullable = false, columnDefinition = "TEXT")
    private String briefing;      // the mystery story shown to the learner

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Difficulty difficulty = Difficulty.BEGINNER;

    /** MySQL schema name query-execution-service should run this case's queries against. */
    @Column(name = "target_schema", nullable = false, length = 100)
    private String targetSchema;

    /**
     * The id (in the case's own {@code person} table) of the correct suspect.
     * Never sent to the frontend — only used server-side to grade an accusation.
     */
    @Column(name = "solution_suspect_id", nullable = false)
    private Integer solutionSuspectId;

    @Column(name = "solution_explanation", length = 1000)
    private String solutionExplanation;   // revealed only after a correct accusation

    @Column(name = "points_reward", nullable = false)
    private Integer pointsReward = 100;

    @Column(name = "display_order", nullable = false)
    private Integer displayOrder = 0;

    public CaseFile() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getBriefing() { return briefing; }
    public void setBriefing(String briefing) { this.briefing = briefing; }

    public Difficulty getDifficulty() { return difficulty; }
    public void setDifficulty(Difficulty difficulty) { this.difficulty = difficulty; }

    public String getTargetSchema() { return targetSchema; }
    public void setTargetSchema(String targetSchema) { this.targetSchema = targetSchema; }

    public Integer getSolutionSuspectId() { return solutionSuspectId; }
    public void setSolutionSuspectId(Integer solutionSuspectId) { this.solutionSuspectId = solutionSuspectId; }

    public String getSolutionExplanation() { return solutionExplanation; }
    public void setSolutionExplanation(String solutionExplanation) { this.solutionExplanation = solutionExplanation; }

    public Integer getPointsReward() { return pointsReward; }
    public void setPointsReward(Integer pointsReward) { this.pointsReward = pointsReward; }

    public Integer getDisplayOrder() { return displayOrder; }
    public void setDisplayOrder(Integer displayOrder) { this.displayOrder = displayOrder; }
}