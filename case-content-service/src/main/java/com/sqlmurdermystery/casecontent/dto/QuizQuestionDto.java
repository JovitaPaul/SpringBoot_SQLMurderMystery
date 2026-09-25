package com.sqlmurdermystery.casecontent.dto;

import java.util.List;

public record QuizQuestionDto(Long id, String questionText, String codeSnippet, Integer points, List<OptionDto> options) {
}
