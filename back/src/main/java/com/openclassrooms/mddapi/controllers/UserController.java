package com.openclassrooms.mddapi.controllers;

import com.openclassrooms.mddapi.dto.UpdatePasswordDto;
import com.openclassrooms.mddapi.dto.UpdateUserDto;
import com.openclassrooms.mddapi.dto.UserDto;
import com.openclassrooms.mddapi.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for handling user profile management operations.
 * This includes retrieving the current user's profile, updating profile information,
 * and changing the user's password.
 */
@RestController
@RequestMapping("/api/me")
public class UserController {

  @Autowired
  private UserService userService;

  /**
   * Retrieves the profile of the currently authenticated user.
   *
   * @return A {@link UserDto} object representing the user's profile.
   */
  @GetMapping
  public UserDto getMyProfile() {
    return userService.getCurrentUserProfile();
  }

  /**
   * Updates the profile of the currently authenticated user.
   *
   * @param updateUserDto The request body containing the updated user details.
   * @return A {@link UserDto} object representing the updated user profile.
   */
  @PutMapping
  public UserDto updateMyProfile(@Valid @RequestBody UpdateUserDto updateUserDto) {
    return userService.updateUserProfile(updateUserDto);
  }

  /**
   * Updates the password of the currently authenticated user.
   *
   * @param updatePasswordDto The request body containing the old and new passwords.
   * @return A confirmation message.
   */
  @PutMapping("/password")
  public String updateMyPassword(@Valid @RequestBody UpdatePasswordDto updatePasswordDto) {
    return userService.updateUserPassword(updatePasswordDto);
  }
}