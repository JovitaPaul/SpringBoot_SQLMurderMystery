package com.sqlmurdermystery.casecontent.repository;

import com.sqlmurdermystery.casecontent.model.QuizAttempt;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuizAttemptRepository extends JpaRepository<QuizAttempt, Long> {
    List<QuizAttempt> findByUsernameAndTopicIdOrderByCompletedAtDesc(String username, Long topicId);
    List<QuizAttempt> findByUsernameOrderByCompletedAtDesc(String username);
}
