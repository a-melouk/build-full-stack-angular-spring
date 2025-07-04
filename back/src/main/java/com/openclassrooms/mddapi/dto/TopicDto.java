package com.openclassrooms.mddapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for topic information.
 * This class represents a topic that users can subscribe to and create posts in.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TopicDto {
    /**
     * The unique identifier of the topic.
     */
    private Long id;

    /**
     * The name of the topic. Cannot be blank and must be at most 50 characters.
     */
    @NotBlank(message = "Name is required")
    @Size(max = 50, message = "Name must be at most 50 characters")
    private String name;

    /**
     * The description of the topic. Must be at most 255 characters.
     */
    @Size(max = 255, message = "Description must be at most 255 characters")
    private String description;
}