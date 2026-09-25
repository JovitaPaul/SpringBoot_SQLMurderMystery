package com.sqlmurdermystery.casecontent.model;

import jakarta.persistence.*;

@Entity
@Table(name = "questions")
public class Question {
    @Id
    @Column(nullable = false)
    private Long id;

    @Column(name = "topic_id")
    private Long topicId;

    @Lob
    @Column(nullable = false, columnDefinition = "TEXT")
    private String question;

    @Column(length = 255)
    private String option1;

    @Column(length = 255)
    private String option2;

    @Column(length = 255)
    private String option3;

    @Column(length = 255)
    private String option4;

    @Column(name = "correct_option", nullable = false)
    private Integer correctOption;

    public Question() {}

    public Long getId() { return id; }
    public Long getTopicId() { return topicId; }
    public String getQuestion() { return question; }
    public String getOption1() { return option1; }
    public String getOption2() { return option2; }
    public String getOption3() { return option3; }
    public String getOption4() { return option4; }
    public Integer getCorrectOption() { return correctOption; }

    public void setId(Long id) { this.id = id; }
    public void setTopicId(Long topicId) { this.topicId = topicId; }
    public void setQuestion(String question) { this.question = question; }
    public void setOption1(String option1) { this.option1 = option1; }
    public void setOption2(String option2) { this.option2 = option2; }
    public void setOption3(String option3) { this.option3 = option3; }
    public void setOption4(String option4) { this.option4 = option4; }
    public void setCorrectOption(Integer correctOption) { this.correctOption = correctOption; }
}
