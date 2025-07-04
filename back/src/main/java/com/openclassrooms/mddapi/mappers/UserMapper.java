package com.openclassrooms.mddapi.mappers;

import com.openclassrooms.mddapi.dto.UserDto;
import com.openclassrooms.mddapi.models.User;
import org.springframework.stereotype.Component;

/**
 * Mapper for converting {@link User} entities to {@link UserDto} Data Transfer Objects.
 */
@Component
public class UserMapper {

  /**
   * Converts a {@link User} entity to a {@link UserDto}.
   *
   * @param user The {@link User} entity to convert.
   * @return The corresponding {@link UserDto}, or null if the input is null.
   */
  public UserDto toDto(User user) {
    if (user == null) {
      return null;
    }
    return UserDto.builder()
        .id(user.getId())
        .email(user.getEmail())
        .username(user.getUsernameField())
        .createdAt(user.getCreatedAt())
        .updatedAt(user.getUpdatedAt())
        .build();
  }

}