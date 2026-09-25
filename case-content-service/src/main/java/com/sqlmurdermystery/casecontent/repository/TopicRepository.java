package com.sqlmurdermystery.casecontent.repository;

import com.sqlmurdermystery.casecontent.model.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TopicRepository extends JpaRepository<Topic, Long> {
    List<Topic> findAllByOrderByIdAsc();
}
