package com.openclassrooms.mddapi.repository;

import com.openclassrooms.mddapi.models.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for {@link Comment} entities.
 * Provides standard CRUD operations and custom query methods for accessing comment data.
 */
@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    /**
     * Finds all comments associated with a specific post.
     *
     * @param postId the ID of the post.
     * @return a list of comments for the given post.
     */
    List<Comment> findByPostId(Long postId);
}