package com.openclassrooms.mddapi.services;

import com.openclassrooms.mddapi.dto.TopicDto;

import java.util.List;

/**
 * Service interface for topic-related operations.
 * Defines methods for retrieving topic information.
 */
public interface TopicService {
    /**
     * Retrieves all topics.
     *
     * @return A list of all {@link TopicDto}s.
     */
    List<TopicDto> findAll();

    /**
     * Retrieves a single topic by its ID.
     *
     * @param id The ID of the topic to retrieve.
     * @return A {@link TopicDto} of the requested topic.
     */
    TopicDto findById(Long id);
}