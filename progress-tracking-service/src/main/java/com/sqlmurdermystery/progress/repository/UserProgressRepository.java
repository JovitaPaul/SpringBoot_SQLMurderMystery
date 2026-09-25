package com.sqlmurdermystery.progress.repository;

import com.sqlmurdermystery.progress.model.UserProgress;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserProgressRepository extends JpaRepository<UserProgress, String> {
}
