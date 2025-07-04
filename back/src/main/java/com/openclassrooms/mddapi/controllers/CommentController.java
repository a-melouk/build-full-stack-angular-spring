package com.openclassrooms.mddapi.controllers;

import com.openclassrooms.mddapi.dto.CreateCommentDto;
import com.openclassrooms.mddapi.dto.CommentDto;
import com.openclassrooms.mddapi.services.CommentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for handling comment-related operations.
 * This includes creating new comments and retrieving comments for a specific post.
 */
@RestController
@RequestMapping("/api/comments")
public class CommentController {

  @Autowired
  private CommentService commentService;

  /**
   * Creates a new comment for a post.
   *
   * @param createCommentDto The request body containing the comment details.
   * @return A {@link CommentDto} representing the newly created comment.
   */
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public CommentDto addComment(@Valid @RequestBody CreateCommentDto createCommentDto) {
    return commentService.createComment(createCommentDto);
  }

  /**
   * Retrieves all comments for a given post.
   *
   * @param postId The ID of the post for which to retrieve comments.
   * @return A list of {@link CommentDto} objects.
   */
  @GetMapping("/post/{postId}")
  public List<CommentDto> getCommentsForPost(@PathVariable Long postId) {
    return commentService.getCommentsByPostId(postId);
  }
}