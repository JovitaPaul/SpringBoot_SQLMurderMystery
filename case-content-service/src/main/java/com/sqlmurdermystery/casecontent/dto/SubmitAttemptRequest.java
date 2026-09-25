package com.sqlmurdermystery.casecontent.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public class SubmitAttemptRequest {

    @NotEmpty
    @Valid
    private List<AnswerSubmission> answers;

    /** How long the learner actually took, reported by the client-side timer. */
    @Min(0)
    private Integer timeTakenSeconds;

    public List<AnswerSubmission> getAnswers() { return answers; }
    public void setAnswers(List<AnswerSubmission> answers) { this.answers = answers; }

    public Integer getTimeTakenSeconds() { return timeTakenSeconds; }
    public void setTimeTakenSeconds(Integer timeTakenSeconds) { this.timeTakenSeconds = timeTakenSeconds; }
}
