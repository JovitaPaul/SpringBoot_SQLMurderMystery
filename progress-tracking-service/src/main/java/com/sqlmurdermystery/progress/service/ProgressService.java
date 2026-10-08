package com.sqlmurdermystery.progress.service;

import com.sqlmurdermystery.progress.dto.ProgressSummaryDto;
import com.sqlmurdermystery.progress.dto.RecordCompletionRequest;
import com.sqlmurdermystery.progress.model.ActivityType;
import org.springframework.dao.ConcurrencyFailureException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
public class ProgressService {

    private final ProgressRecorder recorder;
    private final LeaderboardClient leaderboardClient;
    private final NotificationClient notificationClient;

    public ProgressService(ProgressRecorder recorder,
                           LeaderboardClient leaderboardClient,
                           NotificationClient notificationClient) {
        this.recorder = recorder;
        this.leaderboardClient = leaderboardClient;
        this.notificationClient = notificationClient;
    }

    /** Deliberately NOT @Transactional: the write finishes (and commits) inside the
     *  recorder before the best-effort calls below are made. */
    public ProgressSummaryDto recordCompletion(String username, RecordCompletionRequest request, String bearerToken) {
        RecordResult result = recordWithOneRetry(username, request);

        // Best-effort side effects: failures are logged inside the clients and never
        // undo or fail the learner's own progress record.
        if (result.pointsDelta() > 0) {
            leaderboardClient.addScore(bearerToken, result.pointsDelta());
        }
        if (result.firstCompletion()) {
            ProgressSummaryDto summary = result.summary();
            notificationClient.sendEvent(
                    bearerToken,
                    request.getType() == ActivityType.CASE ? "Case solved!" : "Quiz passed!",
                    (request.getReferenceTitle() != null ? request.getReferenceTitle() : "Nice work")
                            + " — +" + request.getPoints() + " points"
                            + (summary.getCurrentStreak() > 1 ? " (streak: " + summary.getCurrentStreak() + " days)" : "")
            );
        }
        return result.summary();
    }

    public ProgressSummaryDto getSummary(String username) {
        return recorder.summary(username);
    }

    /**
     * Two simultaneous requests (double-click, retry, two tabs) can collide on the unique
     * constraint or on a lock. The loser re-runs once and then sees the winner's row, so
     * it resolves as a harmless repeat instead of an error or a double award.
     */
    private RecordResult recordWithOneRetry(String username, RecordCompletionRequest request) {
        try {
            return recorder.record(username, request);
        } catch (DataIntegrityViolationException | ConcurrencyFailureException e) {
            return recorder.record(username, request);
        }
    }
}
