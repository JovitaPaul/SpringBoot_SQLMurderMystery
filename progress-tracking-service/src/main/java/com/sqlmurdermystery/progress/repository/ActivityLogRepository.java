package com.sqlmurdermystery.progress.repository;

import com.sqlmurdermystery.progress.model.ActivityLog;
import com.sqlmurdermystery.progress.model.ActivityType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ActivityLogRepository extends JpaRepository<ActivityLog, Long> {
    List<ActivityLog> findTop10ByUsernameOrderByOccurredAtDesc(String username);

    Optional<ActivityLog> findByUsernameAndTypeAndReferenceId(String username, ActivityType type, Long referenceId);
}
