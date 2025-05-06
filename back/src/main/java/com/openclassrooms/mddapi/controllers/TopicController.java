package com.openclassrooms.mddapi.controllers;

import com.openclassrooms.mddapi.dto.TopicDto;
import com.openclassrooms.mddapi.payload.response.ApiResponse;
import com.openclassrooms.mddapi.services.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;

import java.util.List;

@RestController
@RequestMapping("/api/topics")
public class TopicController {

    @Autowired
    private TopicService topicService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<TopicDto>>> findAll() {
        try {
            List<TopicDto> topicDtos = this.topicService.findAll();
            ApiResponse<List<TopicDto>> response = ApiResponse.success(topicDtos, "Topics retrieved successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse<List<TopicDto>> errorResponse = ApiResponse.error("Failed to retrieve topics: " + e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}