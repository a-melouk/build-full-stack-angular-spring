package com.openclassrooms.mddapi.controllers;

import com.openclassrooms.mddapi.dto.CreateCommentDto;
import com.openclassrooms.mddapi.dto.CommentDto;
import com.openclassrooms.mddapi.services.CommentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

  @Autowired
  private CommentService commentService;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public CommentDto addComment(@Valid @RequestBody CreateCommentDto createCommentDto) {
    return commentService.createComment(createCommentDto);
  }

  @GetMapping("/post/{postId}")
  public List<CommentDto> getCommentsForPost(@PathVariable Long postId) {
    return commentService.getCommentsByPostId(postId);
  }
}