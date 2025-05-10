package com.openclassrooms.mddapi.services;

import com.openclassrooms.mddapi.dto.TopicDto;

import java.util.List;

public interface TopicService {
    List<TopicDto> findAll();

    TopicDto findById(Long id);
}