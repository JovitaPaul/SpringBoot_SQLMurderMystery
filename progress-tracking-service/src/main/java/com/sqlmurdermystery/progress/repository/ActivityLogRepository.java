package com.sqlmurdermystery.progress.repository;

import com.sqlmurdermystery.progress.model.ActivityLog;
import com.sqlmurdermystery.progress.model.ActivityType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ActivityLogRepository extends JpaRepository<ActivityLog, Long> {

    List<ActivityLog> findTop10ByUsernameOrderByOccurredAtDesc(String username);

    List<ActivityLog> findByUsernameOrderByOccurredAtDesc(String username, Pageable pageable);

    /** Has this learner already completed this exact case / quiz? */
    boolean existsByUsernameAndTypeAndReferenceId(String username, ActivityType type, Long referenceId);

    /** Distinct case or quiz ids the learner has completed — the "solved" set. */
    @Query("select distinct a.referenceId from ActivityLog a " +
           "where a.username = :username and a.type = :type order by a.referenceId")
    List<Long> findCompletedReferenceIds(@Param("username") String username,
                                         @Param("type") ActivityType type);
}
