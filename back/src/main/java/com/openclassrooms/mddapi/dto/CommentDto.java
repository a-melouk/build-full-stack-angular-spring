package com.openclassrooms.mddapi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for comment information.
 * This class represents a comment made by a user on a post.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {
    /**
     * The unique identifier of the comment.
     */
    private Long id;

    /**
     * The content of the comment.
     */
    private String content;

    /**
     * The timestamp when the comment was created.
     */
    private LocalDateTime createdAt;

    /**
     * The username of the user who posted the comment.
     */
    private String username;
}