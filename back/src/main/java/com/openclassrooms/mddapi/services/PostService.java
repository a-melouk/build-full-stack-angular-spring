package com.openclassrooms.mddapi.services;

import com.openclassrooms.mddapi.dto.CreatePostDto;
import com.openclassrooms.mddapi.dto.PostDto;
import jakarta.validation.Valid;
import java.util.List;

public interface PostService {
    List<PostDto> getFeed(String sort);
    PostDto getById(Long id);
    PostDto create(@Valid CreatePostDto createPostDto);
}