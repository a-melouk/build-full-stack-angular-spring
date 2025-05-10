package com.openclassrooms.mddapi.controllers;

import com.openclassrooms.mddapi.dto.UpdatePasswordDto;
import com.openclassrooms.mddapi.dto.UpdateUserDto;
import com.openclassrooms.mddapi.dto.UserDto;
import com.openclassrooms.mddapi.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/me")
public class UserController {

  @Autowired
  private UserService userService;

  @GetMapping
  public UserDto getMyProfile() {
    return userService.getCurrentUserProfile();
  }

  @PutMapping
  public UserDto updateMyProfile(@Valid @RequestBody UpdateUserDto updateUserDto) {
    return userService.updateUserProfile(updateUserDto);
  }

  @PutMapping("/password")
  public String updateMyPassword(@Valid @RequestBody UpdatePasswordDto updatePasswordDto) {
    return userService.updateUserPassword(updatePasswordDto);
  }
}