package com.sqlmurdermystery.casecontent.repository;

import com.sqlmurdermystery.casecontent.model.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuizRepository extends JpaRepository<Quiz, Long> {
    List<Quiz> findAllByOrderByDisplayOrderAsc();
}
