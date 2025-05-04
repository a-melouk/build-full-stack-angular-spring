package com.openclassrooms.mddapi.services;

import com.openclassrooms.mddapi.models.Topic;

import java.util.List;

public interface TopicService {
    List<Topic> findAll();
}