package com.openclassrooms.mddapi.services;

import com.openclassrooms.mddapi.dto.CreatePostDto;
import com.openclassrooms.mddapi.dto.PostDto;
import jakarta.validation.Valid;
import java.util.List;

/**
 * Service interface for post-related operations.
 * Defines methods for creating, retrieving, and managing posts.
 */
public interface PostService {
    /**
     * Retrieves the post feed for the current user, based on their subscriptions.
     *
     * @param sort The sort order for the posts (e.g., "asc" or "desc").
     * @return A list of {@link PostDto}s representing the user's feed.
     */
    List<PostDto> getFeed(String sort);

    /**
     * Retrieves a single post by its ID.
     *
     * @param id The ID of the post to retrieve.
     * @return A {@link PostDto} of the requested post.
     */
    PostDto getById(Long id);

    /**
     * Creates a new post.
     *
     * @param createPostDto DTO containing the details for the new post.
     * @return A {@link PostDto} of the newly created post.
     */
    PostDto create(@Valid CreatePostDto createPostDto);
}