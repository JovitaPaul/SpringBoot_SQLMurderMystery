package com.sqlmurdermystery.progress.service;

import com.sqlmurdermystery.progress.dto.RecordCompletionRequest;
import com.sqlmurdermystery.progress.model.ActivityLog;
import com.sqlmurdermystery.progress.model.ActivityType;
import com.sqlmurdermystery.progress.model.UserProgress;
import com.sqlmurdermystery.progress.repository.ActivityLogRepository;
import com.sqlmurdermystery.progress.repository.UserProgressRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProgressRecorderTest {

    private static final ZoneId IST = ZoneId.of("Asia/Kolkata");
    private static final String USER = "puja";

    @Mock UserProgressRepository userProgressRepository;
    @Mock ActivityLogRepository activityLogRepository;

    @BeforeEach
    void defaultStubs() {
        lenient().when(userProgressRepository.findForUpdate(anyString())).thenReturn(Optional.empty());
        lenient().when(userProgressRepository.save(any(UserProgress.class))).thenAnswer(i -> i.getArgument(0));
        lenient().when(activityLogRepository.save(any(ActivityLog.class))).thenAnswer(i -> i.getArgument(0));
        lenient().when(activityLogRepository.findByUsernameAndTypeAndReferenceId(anyString(), any(), anyLong()))
                .thenReturn(Optional.empty());
        lenient().when(activityLogRepository.findTop10ByUsernameOrderByOccurredAtDesc(anyString()))
                .thenReturn(List.of());
    }

    // ---------- helpers ----------

    private ProgressRecorder recorderOn(String isoDate) {
        Clock clock = Clock.fixed(LocalDate.parse(isoDate).atTime(12, 0).atZone(IST).toInstant(), IST);
        return new ProgressRecorder(userProgressRepository, activityLogRepository, clock);
    }

    private static RecordCompletionRequest request(ActivityType type, long referenceId, int points) {
        RecordCompletionRequest r = new RecordCompletionRequest();
        r.setType(type);
        r.setReferenceId(referenceId);
        r.setReferenceTitle("Title " + referenceId);
        r.setPoints(points);
        return r;
    }

    private static UserProgress progress(int total, int quizzes, int cases, int streak, int longest, String lastDate) {
        UserProgress p = new UserProgress(USER);
        p.setTotalPoints(total);
        p.setQuizzesPassed(quizzes);
        p.setCasesSolved(cases);
        p.setCurrentStreak(streak);
        p.setLongestStreak(longest);
        p.setLastActivityDate(lastDate == null ? null : LocalDate.parse(lastDate));
        return p;
    }

    private static ActivityLog logEntry(ActivityType type, long referenceId, int points) {
        ActivityLog a = new ActivityLog();
        a.setUsername(USER);
        a.setType(type);
        a.setReferenceId(referenceId);
        a.setPointsAwarded(points);
        return a;
    }

    private void existing(UserProgress p, ActivityLog log) {
        when(userProgressRepository.findForUpdate(USER)).thenReturn(Optional.of(p));
        when(activityLogRepository.findByUsernameAndTypeAndReferenceId(USER, log.getType(), log.getReferenceId()))
                .thenReturn(Optional.of(log));
    }

    // ---------- first completion ----------

    @Test
    void firstQuizCompletion_awardsPointsCountsQuizAndStartsStreak() {
        RecordResult result = recorderOn("2026-10-08").record(USER, request(ActivityType.QUIZ, 1, 40));

        assertThat(result.firstCompletion()).isTrue();
        assertThat(result.pointsDelta()).isEqualTo(40);
        assertThat(result.summary().getTotalPoints()).isEqualTo(40);
        assertThat(result.summary().getQuizzesPassed()).isEqualTo(1);
        assertThat(result.summary().getCasesSolved()).isEqualTo(0);
        assertThat(result.summary().getCurrentStreak()).isEqualTo(1);
        verify(activityLogRepository).save(any(ActivityLog.class));
    }

    @Test
    void firstCaseCompletion_countsCaseNotQuiz() {
        RecordResult result = recorderOn("2026-10-08").record(USER, request(ActivityType.CASE, 7, 100));

        assertThat(result.summary().getCasesSolved()).isEqualTo(1);
        assertThat(result.summary().getQuizzesPassed()).isEqualTo(0);
        assertThat(result.summary().getTotalPoints()).isEqualTo(100);
    }

    // ---------- repeats ----------

    @Test
    void repeatWithSamePoints_isANoOpForPointsAndCounters() {
        existing(progress(100, 0, 1, 1, 1, "2026-10-08"), logEntry(ActivityType.CASE, 5, 100));

        RecordResult result = recorderOn("2026-10-08").record(USER, request(ActivityType.CASE, 5, 100));

        assertThat(result.firstCompletion()).isFalse();
        assertThat(result.pointsDelta()).isEqualTo(0);
        assertThat(result.summary().getTotalPoints()).isEqualTo(100);
        assertThat(result.summary().getCasesSolved()).isEqualTo(1);
        verify(activityLogRepository, never()).save(any(ActivityLog.class));
    }

    @Test
    void betterQuizScore_awardsOnlyTheDifferenceAndDoesNotRecount() {
        ActivityLog entry = logEntry(ActivityType.QUIZ, 3, 30);
        existing(progress(30, 1, 0, 1, 1, "2026-10-08"), entry);

        RecordResult result = recorderOn("2026-10-08").record(USER, request(ActivityType.QUIZ, 3, 50));

        assertThat(result.firstCompletion()).isFalse();
        assertThat(result.pointsDelta()).isEqualTo(20);
        assertThat(result.summary().getTotalPoints()).isEqualTo(50);
        assertThat(result.summary().getQuizzesPassed()).isEqualTo(1);
        assertThat(entry.getPointsAwarded()).isEqualTo(50);
    }

    @Test
    void worseQuizScore_awardsNothingAndKeepsBestScore() {
        ActivityLog entry = logEntry(ActivityType.QUIZ, 3, 50);
        existing(progress(50, 1, 0, 1, 1, "2026-10-08"), entry);

        RecordResult result = recorderOn("2026-10-08").record(USER, request(ActivityType.QUIZ, 3, 30));

        assertThat(result.pointsDelta()).isEqualTo(0);
        assertThat(result.summary().getTotalPoints()).isEqualTo(50);
        assertThat(entry.getPointsAwarded()).isEqualTo(50);
    }

    // ---------- streaks ----------

    @Test
    void activityYesterday_extendsStreakAndUpdatesLongest() {
        when(userProgressRepository.findForUpdate(USER))
                .thenReturn(Optional.of(progress(10, 1, 0, 3, 3, "2026-10-07")));

        RecordResult result = recorderOn("2026-10-08").record(USER, request(ActivityType.QUIZ, 2, 10));

        assertThat(result.summary().getCurrentStreak()).isEqualTo(4);
        assertThat(result.summary().getLongestStreak()).isEqualTo(4);
    }

    @Test
    void missedADay_resetsStreakButKeepsLongest() {
        when(userProgressRepository.findForUpdate(USER))
                .thenReturn(Optional.of(progress(10, 1, 0, 5, 5, "2026-10-06")));

        RecordResult result = recorderOn("2026-10-08").record(USER, request(ActivityType.QUIZ, 2, 10));

        assertThat(result.summary().getCurrentStreak()).isEqualTo(1);
        assertThat(result.summary().getLongestStreak()).isEqualTo(5);
    }

    @Test
    void secondActivitySameDay_leavesStreakUnchanged() {
        when(userProgressRepository.findForUpdate(USER))
                .thenReturn(Optional.of(progress(10, 1, 0, 2, 2, "2026-10-08")));

        RecordResult result = recorderOn("2026-10-08").record(USER, request(ActivityType.QUIZ, 2, 10));

        assertThat(result.summary().getCurrentStreak()).isEqualTo(2);
    }

    @Test
    void repeatCompletion_stillCountsAsActivityForTheStreak() {
        existing(progress(100, 0, 1, 2, 2, "2026-10-07"), logEntry(ActivityType.CASE, 5, 100));

        RecordResult result = recorderOn("2026-10-08").record(USER, request(ActivityType.CASE, 5, 100));

        assertThat(result.pointsDelta()).isEqualTo(0);
        assertThat(result.summary().getCurrentStreak()).isEqualTo(3);
    }

    @Test
    void streakDayFollowsConfiguredTimezone_notUtc() {
        // 20:00 UTC on 7 Oct is already 01:30 on 8 Oct in India.
        Clock clock = Clock.fixed(Instant.parse("2026-10-07T20:00:00Z"), IST);
        ProgressRecorder recorder = new ProgressRecorder(userProgressRepository, activityLogRepository, clock);
        when(userProgressRepository.findForUpdate(USER))
                .thenReturn(Optional.of(progress(10, 1, 0, 1, 1, "2026-10-07")));

        RecordResult result = recorder.record(USER, request(ActivityType.QUIZ, 9, 10));

        assertThat(result.summary().getCurrentStreak()).isEqualTo(2);
    }

    // ---------- summary ----------

    @Test
    void summaryForUnknownLearner_isAllZeros() {
        when(userProgressRepository.findById(USER)).thenReturn(Optional.empty());

        var summary = recorderOn("2026-10-08").summary(USER);

        assertThat(summary.getUsername()).isEqualTo(USER);
        assertThat(summary.getTotalPoints()).isEqualTo(0);
        assertThat(summary.getRecentActivity()).isEmpty();
    }
}
