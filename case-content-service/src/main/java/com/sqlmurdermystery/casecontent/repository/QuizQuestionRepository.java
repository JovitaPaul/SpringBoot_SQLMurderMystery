package com.sqlmurdermystery.casecontent.repository;

import com.sqlmurdermystery.casecontent.model.QuizQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizQuestionRepository extends JpaRepository<QuizQuestion, Long> {
}
