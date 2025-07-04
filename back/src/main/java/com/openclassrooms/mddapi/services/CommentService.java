package com.openclassrooms.mddapi.services;

import com.openclassrooms.mddapi.dto.CreateCommentDto;
import com.openclassrooms.mddapi.dto.CommentDto;
import jakarta.validation.Valid;
import java.util.List;

/**
 * Service interface for comment-related operations.
 * Defines methods for creating and retrieving comments.
 */
public interface CommentService {
  /**
   * Creates a new comment.
   *
   * @param createCommentDto DTO containing the details for the new comment.
   * @return A {@link CommentDto} of the newly created comment.
   */
  CommentDto createComment(@Valid CreateCommentDto createCommentDto);

  /**
   * Retrieves all comments for a specific post.
   *
   * @param postId The ID of the post.
   * @return A list of {@link CommentDto}s for the given post.
   */
  List<CommentDto> getCommentsByPostId(Long postId);
}