package com.sqlmurdermystery.casecontent.repository;

import com.sqlmurdermystery.casecontent.model.QuizOption;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizOptionRepository extends JpaRepository<QuizOption, Long> {
}
