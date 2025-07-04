package com.openclassrooms.mddapi.repository;

import com.openclassrooms.mddapi.models.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for {@link Post} entities.
 * Provides standard CRUD operations and custom query methods for accessing post data.
 */
@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
}