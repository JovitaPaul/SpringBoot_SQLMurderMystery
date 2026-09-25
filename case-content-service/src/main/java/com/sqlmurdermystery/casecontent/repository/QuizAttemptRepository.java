package com.sqlmurdermystery.casecontent.repository;

import com.sqlmurdermystery.casecontent.model.QuizAttempt;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuizAttemptRepository extends JpaRepository<QuizAttempt, Long> {
    List<QuizAttempt> findByUsernameAndQuizIdOrderByCompletedAtDesc(String username, Long quizId);
    List<QuizAttempt> findByUsernameOrderByCompletedAtDesc(String username);
}
