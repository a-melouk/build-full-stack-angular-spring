package com.openclassrooms.mddapi.repository;

import com.openclassrooms.mddapi.models.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByTopicId(Long topicId);
    List<Post> findByUserId(Long userId);
    List<Post> findByTopic_IdIn(List<Long> topicIds);
}