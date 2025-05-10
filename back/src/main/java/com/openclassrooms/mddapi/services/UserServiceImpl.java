package com.openclassrooms.mddapi.services;

import com.openclassrooms.mddapi.dto.UpdatePasswordDto;
import com.openclassrooms.mddapi.dto.UpdateUserDto;
import com.openclassrooms.mddapi.dto.UserDto;
import com.openclassrooms.mddapi.exception.BadRequestException;
import com.openclassrooms.mddapi.exception.EmailAlreadyExistsException;
import com.openclassrooms.mddapi.exception.ResourceNotFoundException;
import com.openclassrooms.mddapi.mappers.UserMapper;
import com.openclassrooms.mddapi.models.User;
import com.openclassrooms.mddapi.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
public class UserServiceImpl implements UserService {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private UserMapper userMapper;

  @Autowired
  private PasswordEncoder passwordEncoder;

  private User getAuthenticatedUser() {
    UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    return userRepository.findByEmail(userDetails.getUsername())
        .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + userDetails.getUsername()));
  }

  @Override
  public UserDto getCurrentUserProfile() {
    User user = getAuthenticatedUser();
    return userMapper.toDto(user);
  }

  @Override
  @Transactional
  public UserDto updateUserProfile(@Valid UpdateUserDto updateUserDto) {
    User currentUser = getAuthenticatedUser();

    if (StringUtils.hasText(updateUserDto.getEmail()) && !updateUserDto.getEmail().equals(currentUser.getEmail())) {
      if (userRepository.existsByEmail(updateUserDto.getEmail())) {
        throw new EmailAlreadyExistsException("Email is already taken: " + updateUserDto.getEmail());
      }
      currentUser.setEmail(updateUserDto.getEmail());
    }
    if (StringUtils.hasText(updateUserDto.getFirstName())) {
      currentUser.setFirstName(updateUserDto.getFirstName());
    }
    if (StringUtils.hasText(updateUserDto.getLastName())) {
      currentUser.setLastName(updateUserDto.getLastName());
    }

    User updatedUser = userRepository.save(currentUser);
    return userMapper.toDto(updatedUser);
  }

  @Override
  @Transactional
  public String updateUserPassword(@Valid UpdatePasswordDto updatePasswordDto) {
    User currentUser = getAuthenticatedUser();

    if (!passwordEncoder.matches(updatePasswordDto.getOldPassword(), currentUser.getPassword())) {
      throw new BadRequestException("Incorrect old password.");
    }
    if (updatePasswordDto.getOldPassword().equals(updatePasswordDto.getNewPassword())) {
      throw new BadRequestException("New password cannot be the same as the old password.");
    }

    currentUser.setPassword(passwordEncoder.encode(updatePasswordDto.getNewPassword()));
    userRepository.save(currentUser);
    return "Password updated successfully.";
  }
}