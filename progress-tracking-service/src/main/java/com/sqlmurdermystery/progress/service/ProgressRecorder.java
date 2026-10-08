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

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Owns every database write for progress tracking. Kept separate from ProgressService
 * so the transaction ends before any call to leaderboard/notification is made: a slow
 * downstream service must never hold a database connection open.
 *
 * Rules:
 *  - The first completion of a quiz/case awards its points and bumps the matching counter.
 *  - A repeat of the same quiz/case never bumps the counters again. It only awards the
 *    difference if the new score beats the best one so far (relevant for quizzes, which
 *    can be retaken).
 *  - Any completion, including a repeat, counts as activity for the day's streak.
 */
@Service
public class ProgressRecorder {

    private final UserProgressRepository userProgressRepository;
    private final ActivityLogRepository activityLogRepository;
    private final Clock clock;

    public ProgressRecorder(UserProgressRepository userProgressRepository,
                            ActivityLogRepository activityLogRepository,
                            Clock clock) {
        this.userProgressRepository = userProgressRepository;
        this.activityLogRepository = activityLogRepository;
        this.clock = clock;
    }

    @Transactional
    public RecordResult record(String username, RecordCompletionRequest request) {
        UserProgress progress = userProgressRepository.findForUpdate(username)
                .orElseGet(() -> new UserProgress(username));

        Optional<ActivityLog> existing = activityLogRepository.findByUsernameAndTypeAndReferenceId(
                username, request.getType(), request.getReferenceId());

        int delta;
        boolean first;

        if (existing.isPresent()) {
            ActivityLog entry = existing.get();
            delta = Math.max(0, request.getPoints() - entry.getPointsAwarded());
            first = false;
            if (delta > 0) {
                entry.setPointsAwarded(request.getPoints());
                activityLogRepository.save(entry);
            }
        } else {
            delta = request.getPoints();
            first = true;

            ActivityLog entry = new ActivityLog();
            entry.setUsername(username);
            entry.setType(request.getType());
            entry.setReferenceId(request.getReferenceId());
            entry.setReferenceTitle(request.getReferenceTitle());
            entry.setPointsAwarded(request.getPoints());
            entry.setOccurredAt(clock.instant());
            activityLogRepository.save(entry);

            if (request.getType() == ActivityType.QUIZ) {
                progress.setQuizzesPassed(progress.getQuizzesPassed() + 1);
            } else {
                progress.setCasesSolved(progress.getCasesSolved() + 1);
            }
        }

        progress.setTotalPoints(progress.getTotalPoints() + delta);
        applyStreak(progress);
        userProgressRepository.save(progress);

        return new RecordResult(toSummaryDto(progress), delta, first);
    }

    @Transactional(readOnly = true)
    public ProgressSummaryDto summary(String username) {
        UserProgress progress = userProgressRepository.findById(username)
                .orElseGet(() -> new UserProgress(username));
        return toSummaryDto(progress);
    }

    private void applyStreak(UserProgress progress) {
        LocalDate today = LocalDate.now(clock);
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
        List<ActivityDto> recent = activityLogRepository
                .findTop10ByUsernameOrderByOccurredAtDesc(progress.getUsername()).stream()
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
