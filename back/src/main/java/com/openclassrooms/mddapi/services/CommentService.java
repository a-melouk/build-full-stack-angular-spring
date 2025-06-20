package com.openclassrooms.mddapi.services;

import com.openclassrooms.mddapi.dto.CreateCommentDto;
import com.openclassrooms.mddapi.dto.CommentDto;
import jakarta.validation.Valid;
import java.util.List;

public interface CommentService {
  CommentDto createComment(@Valid CreateCommentDto createCommentDto);

  List<CommentDto> getCommentsByPostId(Long postId);
}