package com.sqlmurdermystery.progress.service;

import com.sqlmurdermystery.progress.dto.ProgressSummaryDto;

/**
 * Outcome of recording one completion.
 *
 * @param summary         the learner's totals after the write
 * @param pointsDelta     points actually added this time (0 for a replay with no better score)
 * @param firstCompletion true only the first time this learner completes this quiz/case
 */
public record RecordResult(ProgressSummaryDto summary, int pointsDelta, boolean firstCompletion) {
}
