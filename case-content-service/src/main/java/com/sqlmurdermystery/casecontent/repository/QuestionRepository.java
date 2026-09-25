package com.sqlmurdermystery.casecontent.repository;

import com.sqlmurdermystery.casecontent.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findByTopicIdOrderByIdAsc(Long topicId);
}
