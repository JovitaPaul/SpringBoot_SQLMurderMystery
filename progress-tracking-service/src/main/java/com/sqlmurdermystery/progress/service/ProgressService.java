package com.sqlmurdermystery.progress.service;

import com.sqlmurdermystery.progress.dto.ActivityDto;
import com.sqlmurdermystery.progress.dto.ProgressSummaryDto;
import com.sqlmurdermystery.progress.dto.RecordCompletionRequest;
import com.sqlmurdermystery.progress.model.ActivityLog;
import com.sqlmurdermystery.progress.model.ActivityType;
import com.sqlmurdermystery.progress.model.UserProgress;
import com.sqlmurdermystery.progress.repository.ActivityLogRepository;
import com.sqlmurdermystery.progress.repository.UserProgressRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class ProgressService {

    private final UserProgressRepository userProgressRepository;
    private final ActivityLogRepository activityLogRepository;
    private final LeaderboardClient leaderboardClient;
    private final NotificationClient notificationClient;

    public ProgressService(UserProgressRepository userProgressRepository,
                            ActivityLogRepository activityLogRepository,
                            LeaderboardClient leaderboardClient,
                            NotificationClient notificationClient) {
        this.userProgressRepository = userProgressRepository;
        this.activityLogRepository = activityLogRepository;
        this.leaderboardClient = leaderboardClient;
        this.notificationClient = notificationClient;
    }

    @Transactional
    public ProgressSummaryDto recordCompletion(String username, RecordCompletionRequest request, String bearerToken) {
        UserProgress progress = userProgressRepository.findById(username)
                .orElseGet(() -> new UserProgress(username));

        progress.setTotalPoints(progress.getTotalPoints() + request.getPoints());
        if (request.getType() == ActivityType.QUIZ) {
            progress.setQuizzesPassed(progress.getQuizzesPassed() + 1);
        } else {
            progress.setCasesSolved(progress.getCasesSolved() + 1);
        }
        applyStreak(progress);
        userProgressRepository.save(progress);

        ActivityLog log = new ActivityLog();
        log.setUsername(username);
        log.setType(request.getType());
        log.setReferenceId(request.getReferenceId());
        log.setReferenceTitle(request.getReferenceTitle());
        log.setPointsAwarded(request.getPoints());
        activityLogRepository.save(log);

        // Best-effort side effects — failures here are logged but never roll back or
        // fail the request above; the learner's own progress record is the source of
        // truth and must be saved regardless of these two calls' outcome.
        leaderboardClient.setScore(bearerToken, progress.getTotalPoints());
        notificationClient.sendEvent(
                bearerToken,
                request.getType() == ActivityType.CASE ? "Case solved!" : "Quiz passed!",
                (request.getReferenceTitle() != null ? request.getReferenceTitle() : "Nice work")
                        + " — +" + request.getPoints() + " points"
                        + (progress.getCurrentStreak() > 1 ? " (streak: " + progress.getCurrentStreak() + " days)" : "")
        );

        return toSummaryDto(progress);
    }

    /**
     * Dashboard read. Also re-syncs the learner's total to the leaderboard (best-effort),
     * so anyone whose earlier leaderboard update was missed — service down, timeout,
     * Redis wiped — is repaired the next time they open their dashboard.
     * Deliberately not @Transactional: no DB connection is held during the REST call.
     */
    public ProgressSummaryDto getSummary(String username, String bearerToken) {
        UserProgress progress = userProgressRepository.findById(username)
                .orElseGet(() -> new UserProgress(username));
        if (bearerToken != null && progress.getTotalPoints() > 0) {
            leaderboardClient.setScore(bearerToken, progress.getTotalPoints());
        }
        return toSummaryDto(progress);
    }

    private void applyStreak(UserProgress progress) {
        LocalDate today = LocalDate.now();
        LocalDate last = progress.getLastActivityDate();

        if (last == null || last.isBefore(today.minusDays(1))) {
            progress.setCurrentStreak(1);
        } else if (last.isEqual(today.minusDays(1))) {
            progress.setCurrentStreak(progress.getCurrentStreak() + 1);
        }
        // else last == today: already counted today, streak unchanged.

        progress.setLastActivityDate(today);
        if (progress.getCurrentStreak() > progress.getLongestStreak()) {
            progress.setLongestStreak(progress.getCurrentStreak());
        }
    }

    private ProgressSummaryDto toSummaryDto(UserProgress progress) {
        List<ActivityDto> recent = progress.getUsername() == null ? List.of() :
                activityLogRepository.findTop10ByUsernameOrderByOccurredAtDesc(progress.getUsername()).stream()
                        .map(a -> new ActivityDto(a.getType().name(), a.getReferenceTitle(), a.getPointsAwarded(), a.getOccurredAt()))
                        .toList();

        return new ProgressSummaryDto(
                progress.getUsername(),
                progress.getTotalPoints(),
                progress.getQuizzesPassed(),
                progress.getCasesSolved(),
                progress.getCurrentStreak(),
                progress.getLongestStreak(),
                recent
        );
    }
}