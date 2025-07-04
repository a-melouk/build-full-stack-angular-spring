package com.openclassrooms.mddapi.services;

import com.openclassrooms.mddapi.dto.UpdatePasswordDto;
import com.openclassrooms.mddapi.dto.UpdateUserDto;
import com.openclassrooms.mddapi.dto.UserDto;
import jakarta.validation.Valid;

/**
 * Service interface for user-related operations.
 * Defines methods for managing the current user's profile and password.
 */
public interface UserService {
  /**
   * Retrieves the profile of the currently authenticated user.
   *
   * @return A {@link UserDto} representing the user's profile.
   */
  UserDto getCurrentUserProfile();

  /**
   * Updates the profile of the currently authenticated user.
   *
   * @param updateUserDto DTO containing the updated user information.
   * @return A {@link UserDto} of the updated user profile.
   */
  UserDto updateUserProfile(@Valid UpdateUserDto updateUserDto);

  /**
   * Updates the password of the currently authenticated user.
   *
   * @param updatePasswordDto DTO containing the old and new passwords.
   * @return A confirmation message.
   */
  String updateUserPassword(@Valid UpdatePasswordDto updatePasswordDto);
}