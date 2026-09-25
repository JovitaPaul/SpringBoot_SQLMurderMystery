package com.sqlmurdermystery.casecontent.model;

import jakarta.persistence.*;

@Entity
// Deliberately NOT "quiz_attempt_answers" — see the comment on QuizAttempt's
// @Table for why. Fresh table, created by Hibernate, old one left alone.
@Table(name = "topic_quiz_attempt_answers")
public class QuizAttemptAnswer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attempt_id", nullable = false)
    private QuizAttempt attempt;

    @Column(name = "question_id", nullable = false)
    private Long questionId;

    @Column(name = "selected_option_id")
    private Long selectedOptionId;

    @Column(nullable = false)
    private boolean correct = false;

    public QuizAttemptAnswer() {}

    public QuizAttemptAnswer(QuizAttempt attempt, Long questionId, Long selectedOptionId, boolean correct) {
        this.attempt = attempt;
        this.questionId = questionId;
        this.selectedOptionId = selectedOptionId;
        this.correct = correct;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public QuizAttempt getAttempt() { return attempt; }
    public void setAttempt(QuizAttempt attempt) { this.attempt = attempt; }
    public Long getQuestionId() { return questionId; }
    public void setQuestionId(Long questionId) { this.questionId = questionId; }
    public Long getSelectedOptionId() { return selectedOptionId; }
    public void setSelectedOptionId(Long selectedOptionId) { this.selectedOptionId = selectedOptionId; }
    public boolean isCorrect() { return correct; }
    public void setCorrect(boolean correct) { this.correct = correct; }
}
