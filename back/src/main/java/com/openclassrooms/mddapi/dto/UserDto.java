package com.openclassrooms.mddapi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for user information.
 * This class represents a user's public profile data.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
  /**
   * The unique identifier of the user.
   */
  private Long id;

  /**
   * The email address of the user.
   */
  private String email;

  /**
   * The username of the user.
   */
  private String username;

  /**
   * The timestamp when the user account was created.
   */
  private LocalDateTime createdAt;

  /**
   * The timestamp when the user account was last updated.
   */
  private LocalDateTime updatedAt;
}