package com.sqlmurdermystery.casecontent.dto;

public record QuizSummaryDto(Long id, String topic, String description, String difficulty,
                              Integer timeLimitSeconds, Integer questionCount) {
}
