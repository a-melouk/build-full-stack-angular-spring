package com.openclassrooms.mddapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for creating a new comment.
 * This class contains the necessary information to associate a new comment with a post.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateCommentDto {

  /**
   * The content of the comment. Cannot be blank.
   */
  @NotBlank(message = "Content is required")
  private String content;

  /**
   * The ID of the post to which the comment belongs. Cannot be null.
   */
  @NotNull(message = "Post ID is required")
  private Long postId;
}