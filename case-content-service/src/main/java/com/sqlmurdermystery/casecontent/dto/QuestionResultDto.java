package com.sqlmurdermystery.casecontent.dto;

public record QuestionResultDto(Long questionId, boolean correct, Long selectedOptionId, Long correctOptionId) {
}
