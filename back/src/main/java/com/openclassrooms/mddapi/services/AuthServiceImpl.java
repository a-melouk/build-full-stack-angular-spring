package com.openclassrooms.mddapi.services;

import com.openclassrooms.mddapi.dto.AuthResponse;
import com.openclassrooms.mddapi.dto.UserUpdateDto;
import com.openclassrooms.mddapi.exception.EmailAlreadyExistsException;
import com.openclassrooms.mddapi.exception.ResourceNotFoundException;
import com.openclassrooms.mddapi.models.User;
import com.openclassrooms.mddapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public AuthResponse updateProfile(UserUpdateDto userUpdateDto, String currentUserEmail) {
        User user = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + currentUserEmail));

        // Validate password confirmation if password is provided
        if (userUpdateDto.getPassword() != null && !userUpdateDto.getPassword().isEmpty()) {
            if (!userUpdateDto.getPassword().equals(userUpdateDto.getPasswordConfirmation())) {
                throw new IllegalArgumentException("Password and confirmation do not match");
            }
            user.setPassword(passwordEncoder.encode(userUpdateDto.getPassword()));
        }

        // Check email uniqueness if email is being updated
        if (userUpdateDto.getEmail() != null && !userUpdateDto.getEmail().equals(user.getEmail())) {
            if (userRepository.existsByEmail(userUpdateDto.getEmail())) {
                throw new EmailAlreadyExistsException("Email is already taken!");
            }
            user.setEmail(userUpdateDto.getEmail());
        }

        // Check username uniqueness if username is being updated
        if (userUpdateDto.getUsername() != null && !userUpdateDto.getUsername().equals(user.getUsername())) {
            if (userRepository.existsByUsername(userUpdateDto.getUsername())) {
                throw new EmailAlreadyExistsException("Username is already taken!");
            }
            user.setUsername(userUpdateDto.getUsername());
        }

        User updatedUser = userRepository.save(user);

        return AuthResponse.builder()
                .id(updatedUser.getId())
                .email(updatedUser.getEmail())
                .username(updatedUser.getUsernameField())
                .build();
    }

    @Override
    public AuthResponse getCurrentUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + email));

        return AuthResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .username(user.getUsernameField())
                .build();
    }
}