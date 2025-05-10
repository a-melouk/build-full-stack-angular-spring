package com.openclassrooms.mddapi.mappers;

import com.openclassrooms.mddapi.dto.CommentDto;
import com.openclassrooms.mddapi.models.Comment;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CommentMapper {

  public CommentDto toDto(Comment comment) {
    if (comment == null) {
      return null;
    }
    return CommentDto.builder()
        .id(comment.getId())
        .content(comment.getContent())
        .createdAt(comment.getCreatedAt())
        .authorName(comment.getUser().getFirstName() + " " + comment.getUser().getLastName())
        .build();
  }

  public List<CommentDto> toDtoList(List<Comment> comments) {
    if (comments == null) {
      return null;
    }
    return comments.stream()
        .map(this::toDto)
        .collect(Collectors.toList());
  }
}