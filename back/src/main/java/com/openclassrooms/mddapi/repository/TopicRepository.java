package com.openclassrooms.mddapi.repository;

import com.openclassrooms.mddapi.models.Topic;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TopicRepository extends JpaRepository<Topic, Long> {
    @EntityGraph(attributePaths = {"posts"})
    List<Topic> findByIdIn(List<Long> topicIds);
}
