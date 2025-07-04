package com.openclassrooms.mddapi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Data Transfer Object for post information.
 * This class represents a post, including its content, author, associated topic, and comments.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostDto {
    /**
     * The unique identifier of the post.
     */
    private Long id;

    /**
     * The title of the post.
     */
    private String title;

    /**
     * The content of the post.
     */
    private String content;

    /**
     * The timestamp when the post was created.
     */
    private LocalDateTime createdAt;

    /**
     * The username of the user who created the post.
     */
    private String username;

    /**
     * The ID of the topic to which the post belongs.
     */
    private Long topicId;

    /**
     * The name of the topic to which the post belongs.
     */
    private String topicName;

    /**
     * A list of comments associated with the post.
     */
    private List<CommentDto> comments;
}