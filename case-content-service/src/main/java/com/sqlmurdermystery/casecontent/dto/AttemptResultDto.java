package com.sqlmurdermystery.casecontent.dto;

import java.util.List;

public record AttemptResultDto(Long attemptId, Long quizId, Integer score, Integer totalQuestions,
                                Integer correctCount, boolean passed, Integer timeTakenSeconds,
                                List<QuestionResultDto> breakdown) {
}
