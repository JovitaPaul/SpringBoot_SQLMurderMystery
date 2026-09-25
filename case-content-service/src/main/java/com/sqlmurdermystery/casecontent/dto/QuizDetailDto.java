package com.sqlmurdermystery.casecontent.dto;

import java.util.List;

public record QuizDetailDto(Long id, String topic, String description, String difficulty,
                             Integer timeLimitSeconds, List<QuizQuestionDto> questions) {
}
