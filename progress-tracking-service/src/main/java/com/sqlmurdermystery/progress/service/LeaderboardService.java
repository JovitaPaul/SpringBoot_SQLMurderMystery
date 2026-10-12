package com.sqlmurdermystery.progress.service;

import com.sqlmurdermystery.progress.dto.LeaderboardEntryDto;
import com.sqlmurdermystery.progress.dto.MyRankDto;
import com.sqlmurdermystery.progress.model.UserProgress;
import com.sqlmurdermystery.progress.repository.UserProgressRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Leaderboard computed straight from user_progress.total_points — the same row that
 * recordCompletion updates — so there is no second store to keep in sync.
 * Ties share a rank (1, 2, 2, 4).
 */
@Service
@Transactional(readOnly = true)
public class LeaderboardService {

    private final UserProgressRepository repository;

    public LeaderboardService(UserProgressRepository repository) {
        this.repository = repository;
    }

    public List<LeaderboardEntryDto> getTop(int limit) {
        List<UserProgress> rows = repository
                .findByTotalPointsGreaterThanOrderByTotalPointsDescUsernameAsc(0, PageRequest.of(0, limit));

        List<LeaderboardEntryDto> result = new ArrayList<>();
        int rank = 0;
        Integer previousPoints = null;
        for (int i = 0; i < rows.size(); i++) {
            UserProgress row = rows.get(i);
            if (!row.getTotalPoints().equals(previousPoints)) {
                rank = i + 1;
                previousPoints = row.getTotalPoints();
            }
            result.add(new LeaderboardEntryDto(rank, row.getUsername(), row.getTotalPoints()));
        }
        return result;
    }

    public MyRankDto getMyRank(String username) {
        int points = repository.findById(username).map(UserProgress::getTotalPoints).orElse(0);
        Integer rank = points > 0 ? (int) repository.countByTotalPointsGreaterThan(points) + 1 : null;
        return new MyRankDto(username, points, rank);
    }
}
