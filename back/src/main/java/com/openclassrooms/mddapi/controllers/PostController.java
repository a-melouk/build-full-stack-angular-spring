package com.openclassrooms.mddapi.controllers;

import com.openclassrooms.mddapi.dto.CreatePostDto;
import com.openclassrooms.mddapi.dto.PostDto;
import com.openclassrooms.mddapi.services.PostService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

/**
 * Controller for handling post-related operations.
 * This includes retrieving posts for the user's feed, fetching a single post by its ID,
 * and creating new posts.
 */
@RestController
@RequestMapping("/api/posts")
public class PostController {

    @Autowired
    private PostService postService;

    /**
     * Retrieves the feed of posts for the currently authenticated user.
     * The feed is sorted by creation date.
     *
     * @param sort The sort order, "asc" for ascending or "desc" for descending. Defaults to "desc".
     * @return A list of {@link PostDto} objects representing the user's feed.
     */
    @GetMapping
    public List<PostDto> getFeed(@RequestParam(value = "sort", defaultValue = "desc") String sort) {
        return postService.getFeed(sort);
    }

    /**
     * Retrieves a single post by its unique identifier.
     *
     * @param id The ID of the post to retrieve.
     * @return A {@link PostDto} object representing the requested post.
     */
    @GetMapping("/{id}")
    public PostDto getById(@PathVariable Long id) {
        return postService.getById(id);
    }

    /**
     * Creates a new post.
     *
     * @param createPostDto The request body containing the details of the post to be created.
     * @return A {@link PostDto} object representing the newly created post.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PostDto create(@Valid @RequestBody CreatePostDto createPostDto) {
        return postService.create(createPostDto);
    }
}