package com.sqlmurdermystery.progress.repository;

import com.sqlmurdermystery.progress.model.UserProgress;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserProgressRepository extends JpaRepository<UserProgress, String> {

    /**
     * Loads the learner's row with SELECT ... FOR UPDATE so two simultaneous completions
     * for the same learner are applied one after the other instead of overwriting each
     * other's totals.
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select p from UserProgress p where p.username = :username")
    Optional<UserProgress> findForUpdate(@Param("username") String username);
}
