package com.sqlmurdermystery.progress.service;

import com.sqlmurdermystery.progress.dto.ActivityDto;
import com.sqlmurdermystery.progress.dto.ProgressSummaryDto;
import com.sqlmurdermystery.progress.dto.RecordCompletionRequest;
import com.sqlmurdermystery.progress.model.ActivityLog;
import com.sqlmurdermystery.progress.model.ActivityType;
import com.sqlmurdermystery.progress.model.UserProgress;
import com.sqlmurdermystery.progress.repository.ActivityLogRepository;
import com.sqlmurdermystery.progress.repository.UserProgressRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class ProgressService {

    private final UserProgressRepository userProgressRepository;
    private final ActivityLogRepository activityLogRepository;
    private final NotificationClient notificationClient;

    public ProgressService(UserProgressRepository userProgressRepository,
                            ActivityLogRepository activityLogRepository,
                            NotificationClient notificationClient) {
        this.userProgressRepository = userProgressRepository;
        this.activityLogRepository = activityLogRepository;
        this.notificationClient = notificationClient;
    }

    /**
     * Records a quiz pass / case solve. Each distinct case or quiz counts ONCE: points,
     * the solved/passed counters and the activity log are only touched the first time.
     * Re-solving something still counts as activity for today's streak, but nothing else.
     */
    @Transactional
    public ProgressSummaryDto recordCompletion(String username, RecordCompletionRequest request, String bearerToken) {
        UserProgress progress = userProgressRepository.findById(username)
                .orElseGet(() -> new UserProgress(username));

        boolean alreadyCompleted = activityLogRepository.existsByUsernameAndTypeAndReferenceId(
                username, request.getType(), request.getReferenceId());

        applyStreak(progress);

        if (!alreadyCompleted) {
            progress.setTotalPoints(progress.getTotalPoints() + request.getPoints());
            if (request.getType() == ActivityType.QUIZ) {
                progress.setQuizzesPassed(progress.getQuizzesPassed() + 1);
            } else {
                progress.setCasesSolved(progress.getCasesSolved() + 1);
            }
        }
        userProgressRepository.save(progress);

        if (!alreadyCompleted) {
            ActivityLog log = new ActivityLog();
            log.setUsername(username);
            log.setType(request.getType());
            log.setReferenceId(request.getReferenceId());
            log.setReferenceTitle(request.getReferenceTitle());
            log.setPointsAwarded(request.getPoints());
            activityLogRepository.save(log);

            // Best-effort — a notification failure never rolls back or fails the request.
            notificationClient.sendEvent(
                    bearerToken,
                    request.getType() == ActivityType.CASE ? "Case solved!" : "Quiz passed!",
                    (request.getReferenceTitle() != null ? request.getReferenceTitle() : "Nice work")
                            + " — +" + request.getPoints() + " points"
                            + (progress.getCurrentStreak() > 1 ? " (streak: " + progress.getCurrentStreak() + " days)" : "")
            );
        }

        return toSummaryDto(progress);
    }

    /** Dashboard / profile read. */
    @Transactional(readOnly = true)
    public ProgressSummaryDto getSummary(String username, String bearerToken) {
        UserProgress progress = userProgressRepository.findById(username)
                .orElseGet(() -> new UserProgress(username));
        return toSummaryDto(progress);
    }

    /** Full activity history (newest first), capped for safety. */
    @Transactional(readOnly = true)
    public List<ActivityDto> getActivity(String username, int limit) {
        return activityLogRepository.findByUsernameOrderByOccurredAtDesc(username, PageRequest.of(0, limit)).stream()
                .map(a -> new ActivityDto(a.getType().name(), a.getReferenceTitle(), a.getPointsAwarded(), a.getOccurredAt()))
                .toList();
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
        String username = progress.getUsername();

        List<ActivityDto> recent = activityLogRepository.findTop10ByUsernameOrderByOccurredAtDesc(username).stream()
                .map(a -> new ActivityDto(a.getType().name(), a.getReferenceTitle(), a.getPointsAwarded(), a.getOccurredAt()))
                .toList();

        // The solved/passed sets come from the activity log, so the counts shown are
        // always "distinct things completed" and can never exceed the total available.
        List<Long> solvedCaseIds = activityLogRepository.findCompletedReferenceIds(username, ActivityType.CASE);
        List<Long> passedQuizIds = activityLogRepository.findCompletedReferenceIds(username, ActivityType.QUIZ);

        return new ProgressSummaryDto(
                username,
                progress.getTotalPoints(),
                passedQuizIds.size(),
                solvedCaseIds.size(),
                progress.getCurrentStreak(),
                progress.getLongestStreak(),
                progress.getLastActivityDate(),
                solvedCaseIds,
                passedQuizIds,
                recent
        );
    }
}
