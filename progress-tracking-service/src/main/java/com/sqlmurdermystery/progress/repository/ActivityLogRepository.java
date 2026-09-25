package com.sqlmurdermystery.progress.repository;

import com.sqlmurdermystery.progress.model.ActivityLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ActivityLogRepository extends JpaRepository<ActivityLog, Long> {
    List<ActivityLog> findTop10ByUsernameOrderByOccurredAtDesc(String username);
}
