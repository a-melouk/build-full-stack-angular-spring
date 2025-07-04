package com.openclassrooms.mddapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for creating a new post.
 * This class contains the necessary information to create a post, including its title, content, and associated topic.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreatePostDto {
    /**
     * The title of the post. Cannot be blank and must be at most 100 characters.
     */
    @NotBlank(message = "Title is required")
    @Size(max = 100, message = "Title must be at most 100 characters")
    private String title;

    /**
     * The content of the post. Cannot be blank.
     */
    @NotBlank(message = "Content is required")
    private String content;

    /**
     * The ID of the topic to which the post belongs. Cannot be null.
     */
    @NotNull(message = "Topic ID is required")
    private Long topicId;
}