package com.openclassrooms.mddapi.services;

import com.openclassrooms.mddapi.dto.AuthResponse;
import com.openclassrooms.mddapi.dto.UserUpdateDto;
import com.openclassrooms.mddapi.exception.EmailAlreadyExistsException;
import com.openclassrooms.mddapi.exception.ResourceNotFoundException;
import com.openclassrooms.mddapi.models.User;
import com.openclassrooms.mddapi.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service implementation for authentication-related operations.
 */
@Service
public class AuthServiceImpl implements AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public AuthResponse updateProfile(UserUpdateDto userUpdateDto, String currentUserEmail) {
        User user = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + currentUserEmail));

        logger.info("Updating profile for user: {}", currentUserEmail);
        boolean hasChanges = false;

        // Validate and update password if provided
        if (userUpdateDto.getPassword() != null && !userUpdateDto.getPassword().trim().isEmpty()) {
            if (userUpdateDto.getPasswordConfirmation() == null ||
                !userUpdateDto.getPassword().equals(userUpdateDto.getPasswordConfirmation())) {
                throw new IllegalArgumentException("Le mot de passe et la confirmation ne correspondent pas");
            }
            user.setPassword(passwordEncoder.encode(userUpdateDto.getPassword()));
            hasChanges = true;
            logger.info("Password updated for user: {}", currentUserEmail);
        }

        // Check and update email if provided and different
        if (userUpdateDto.getEmail() != null && !userUpdateDto.getEmail().trim().isEmpty()) {
            String newEmail = userUpdateDto.getEmail().trim();
            if (!newEmail.equals(user.getEmail())) {
                if (userRepository.existsByEmail(newEmail)) {
                    throw new EmailAlreadyExistsException("Cette adresse email est déjà utilisée !");
                }
                user.setEmail(newEmail);
                hasChanges = true;
                logger.info("Email updated for user: {} -> {}", currentUserEmail, newEmail);
            }
        }

        // Check and update username if provided and different
        if (userUpdateDto.getUsername() != null && !userUpdateDto.getUsername().trim().isEmpty()) {
            String newUsername = userUpdateDto.getUsername().trim();
            if (!newUsername.equals(user.getUsernameField())) {
                if (userRepository.existsByUsername(newUsername)) {
                    throw new EmailAlreadyExistsException("Ce nom d'utilisateur est déjà utilisé !");
                }
                user.setUsername(newUsername);
                hasChanges = true;
                logger.info("Username updated for user: {} -> {}", currentUserEmail, newUsername);
            }
        }

        if (!hasChanges) {
            logger.info("No changes detected for user: {}", currentUserEmail);
        }

        User updatedUser = userRepository.save(user);

        return AuthResponse.builder()
                .id(updatedUser.getId())
                .email(updatedUser.getEmail())
                .username(updatedUser.getUsernameField())
                .build();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AuthResponse getCurrentUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé : " + email));

        return AuthResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .username(user.getUsernameField())
                .build();
    }
}