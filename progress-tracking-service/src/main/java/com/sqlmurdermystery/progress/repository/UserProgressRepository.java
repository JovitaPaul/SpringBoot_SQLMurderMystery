package com.sqlmurdermystery.progress.repository;

import com.sqlmurdermystery.progress.model.UserProgress;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserProgressRepository extends JpaRepository<UserProgress, String> {

    /** Top learners by points; username breaks ties so ordering is stable. */
    List<UserProgress> findByTotalPointsGreaterThanOrderByTotalPointsDescUsernameAsc(int minPoints, Pageable pageable);

    /** How many learners are strictly ahead of a given score (rank = this + 1). */
    long countByTotalPointsGreaterThan(int points);
}
