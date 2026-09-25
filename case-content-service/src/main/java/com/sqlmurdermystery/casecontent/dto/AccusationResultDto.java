package com.sqlmurdermystery.casecontent.dto;

public class AccusationResultDto {
    private boolean correct;
    private String message;
    private String solutionExplanation;   // populated only when correct
    private Integer pointsAwarded;        // populated only when correct

    public AccusationResultDto() {}

    public AccusationResultDto(boolean correct, String message, String solutionExplanation, Integer pointsAwarded) {
        this.correct = correct;
        this.message = message;
        this.solutionExplanation = solutionExplanation;
        this.pointsAwarded = pointsAwarded;
    }

    public boolean isCorrect() { return correct; }
    public void setCorrect(boolean correct) { this.correct = correct; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getSolutionExplanation() { return solutionExplanation; }
    public void setSolutionExplanation(String solutionExplanation) { this.solutionExplanation = solutionExplanation; }

    public Integer getPointsAwarded() { return pointsAwarded; }
    public void setPointsAwarded(Integer pointsAwarded) { this.pointsAwarded = pointsAwarded; }
}
