package com.sqlmurdermystery.progress.service;

import com.sqlmurdermystery.progress.dto.ProgressSummaryDto;
import com.sqlmurdermystery.progress.dto.RecordCompletionRequest;
import com.sqlmurdermystery.progress.model.ActivityType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProgressServiceTest {

    private static final String USER = "puja";
    private static final String TOKEN = "Bearer abc";

    @Mock ProgressRecorder recorder;
    @Mock LeaderboardClient leaderboardClient;
    @Mock NotificationClient notificationClient;
    @InjectMocks ProgressService service;

    private static RecordCompletionRequest request(ActivityType type, int points) {
        RecordCompletionRequest r = new RecordCompletionRequest();
        r.setType(type);
        r.setReferenceId(1L);
        r.setReferenceTitle("SELECT basics");
        r.setPoints(points);
        return r;
    }

    private static ProgressSummaryDto summary(int total) {
        return new ProgressSummaryDto(USER, total, 1, 0, 1, 1, List.of());
    }

    @Test
    void firstCompletion_updatesLeaderboardAndSendsNotification() {
        RecordCompletionRequest req = request(ActivityType.QUIZ, 40);
        when(recorder.record(USER, req)).thenReturn(new RecordResult(summary(40), 40, true));

        ProgressSummaryDto result = service.recordCompletion(USER, req, TOKEN);

        assertThat(result.getTotalPoints()).isEqualTo(40);
        verify(leaderboardClient).addScore(TOKEN, 40);
        verify(notificationClient).sendEvent(eq(TOKEN), eq("Quiz passed!"), contains("+40 points"));
    }

    @Test
    void caseCompletion_usesCaseNotificationTitle() {
        RecordCompletionRequest req = request(ActivityType.CASE, 100);
        when(recorder.record(USER, req)).thenReturn(new RecordResult(summary(100), 100, true));

        service.recordCompletion(USER, req, TOKEN);

        verify(notificationClient).sendEvent(eq(TOKEN), eq("Case solved!"), anyString());
    }

    @Test
    void improvedScore_updatesLeaderboardWithDifferenceOnly_noNotification() {
        RecordCompletionRequest req = request(ActivityType.QUIZ, 50);
        when(recorder.record(USER, req)).thenReturn(new RecordResult(summary(50), 20, false));

        service.recordCompletion(USER, req, TOKEN);

        verify(leaderboardClient).addScore(TOKEN, 20);
        verify(notificationClient, never()).sendEvent(any(), any(), any());
    }

    @Test
    void pureRepeat_touchesNeitherLeaderboardNorNotifications() {
        RecordCompletionRequest req = request(ActivityType.CASE, 100);
        when(recorder.record(USER, req)).thenReturn(new RecordResult(summary(100), 0, false));

        service.recordCompletion(USER, req, TOKEN);

        verify(leaderboardClient, never()).addScore(any(), anyInt());
        verify(notificationClient, never()).sendEvent(any(), any(), any());
    }

    @Test
    void concurrentDuplicate_isRetriedOnceAndResolvesAsRepeat() {
        RecordCompletionRequest req = request(ActivityType.CASE, 100);
        when(recorder.record(USER, req))
                .thenThrow(new DataIntegrityViolationException("duplicate key"))
                .thenReturn(new RecordResult(summary(100), 0, false));

        ProgressSummaryDto result = service.recordCompletion(USER, req, TOKEN);

        assertThat(result.getTotalPoints()).isEqualTo(100);
        verify(recorder, times(2)).record(USER, req);
        verify(leaderboardClient, never()).addScore(any(), anyInt());
    }

    @Test
    void persistentFailure_isNotSwallowed() {
        RecordCompletionRequest req = request(ActivityType.CASE, 100);
        when(recorder.record(USER, req)).thenThrow(new DataIntegrityViolationException("still broken"));

        assertThatThrownBy(() -> service.recordCompletion(USER, req, TOKEN))
                .isInstanceOf(DataIntegrityViolationException.class);
        verify(recorder, times(2)).record(USER, req);
        verify(leaderboardClient, never()).addScore(any(), anyInt());
    }
}
