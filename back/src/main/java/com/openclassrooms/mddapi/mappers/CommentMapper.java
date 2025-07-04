package com.openclassrooms.mddapi.mappers;

import com.openclassrooms.mddapi.dto.CommentDto;
import com.openclassrooms.mddapi.models.Comment;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper for converting {@link Comment} entities to {@link CommentDto} Data Transfer Objects.
 */
@Component
public class CommentMapper {

  /**
   * Converts a {@link Comment} entity to a {@link CommentDto}.
   *
   * @param comment The {@link Comment} entity to convert.
   * @return The corresponding {@link CommentDto}, or null if the input is null.
   */
  public CommentDto toDto(Comment comment) {
    if (comment == null) {
      return null;
    }
    return CommentDto.builder()
        .id(comment.getId())
        .content(comment.getContent())
        .createdAt(comment.getCreatedAt())
        .username(comment.getUser() != null ? comment.getUser().getUsernameField() : null)
        .build();
  }

  /**
   * Converts a list of {@link Comment} entities to a list of {@link CommentDto}s.
   *
   * @param comments The list of {@link Comment} entities to convert.
   * @return A list of {@link CommentDto}s, or null if the input list is null.
   */
  public List<CommentDto> toDtoList(List<Comment> comments) {
    if (comments == null) {
      return null;
    }
    return comments.stream()
        .map(this::toDto)
        .collect(Collectors.toList());
  }
}