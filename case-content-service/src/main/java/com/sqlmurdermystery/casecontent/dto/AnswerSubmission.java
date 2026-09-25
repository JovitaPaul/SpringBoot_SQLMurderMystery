package com.sqlmurdermystery.casecontent.dto;

import jakarta.validation.constraints.NotNull;

/** One answer in a submitted attempt. selectedOptionId may be null if the learner skipped it. */
public class AnswerSubmission {

    @NotNull
    private Long questionId;

    private Long selectedOptionId;

    public Long getQuestionId() { return questionId; }
    public void setQuestionId(Long questionId) { this.questionId = questionId; }

    public Long getSelectedOptionId() { return selectedOptionId; }
    public void setSelectedOptionId(Long selectedOptionId) { this.selectedOptionId = selectedOptionId; }
}
