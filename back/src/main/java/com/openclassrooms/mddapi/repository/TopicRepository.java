package com.openclassrooms.mddapi.repository;

import com.openclassrooms.mddapi.models.Topic;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for {@link Topic} entities.
 * Provides standard CRUD operations and custom query methods for accessing topic data.
 */
@Repository
public interface TopicRepository extends JpaRepository<Topic, Long> {
    /**
     * Finds all topics with the given IDs and fetches their associated posts eagerly.
     *
     * @param topicIds a list of topic IDs.
     * @return a list of topics with their posts.
     */
    @EntityGraph(attributePaths = {"posts"})
    List<Topic> findByIdIn(List<Long> topicIds);
}
