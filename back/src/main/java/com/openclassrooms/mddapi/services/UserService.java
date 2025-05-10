package com.openclassrooms.mddapi.services;

import com.openclassrooms.mddapi.dto.UpdatePasswordDto;
import com.openclassrooms.mddapi.dto.UpdateUserDto;
import com.openclassrooms.mddapi.dto.UserDto;
import jakarta.validation.Valid;

public interface UserService {
  UserDto getCurrentUserProfile();

  UserDto updateUserProfile(@Valid UpdateUserDto updateUserDto);

  String updateUserPassword(@Valid UpdatePasswordDto updatePasswordDto);
}