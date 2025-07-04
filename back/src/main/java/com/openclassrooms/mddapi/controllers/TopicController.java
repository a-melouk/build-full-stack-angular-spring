package com.openclassrooms.mddapi.controllers;

import com.openclassrooms.mddapi.dto.TopicDto;
import com.openclassrooms.mddapi.services.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller for handling topic-related operations.
 * This includes retrieving all available topics and fetching a single topic by its ID.
 */
@RestController
@RequestMapping("/api/topics")
public class TopicController {

    @Autowired
    private TopicService topicService;

    /**
     * Retrieves a list of all topics.
     *
     * @return A list of {@link TopicDto} objects.
     */
    @GetMapping
    public List<TopicDto> findAll() {
        return this.topicService.findAll();
    }

    /**
     * Retrieves a single topic by its unique identifier.
     *
     * @param id The ID of the topic to retrieve.
     * @return A {@link TopicDto} object representing the requested topic.
     */
    @GetMapping("/{id}")
    public TopicDto findById(@PathVariable Long id) {
        return this.topicService.findById(id);
    }
}