package com.openclassrooms.mddapi.services;

import com.openclassrooms.mddapi.dto.UserUpdateDto;
import com.openclassrooms.mddapi.dto.AuthResponse;

/**
 * Service interface for authentication-related operations.
 * Defines methods for managing user profile information.
 */
public interface AuthService {
    /**
     * Updates the profile of the current user.
     *
     * @param userUpdateDto    DTO containing the updated user information.
     * @param currentUserEmail The email of the currently authenticated user.
     * @return An {@link AuthResponse} with the updated user details.
     */
    AuthResponse updateProfile(UserUpdateDto userUpdateDto, String currentUserEmail);

    /**
     * Retrieves the details of the currently authenticated user.
     *
     * @param email The email of the user to retrieve.
     * @return An {@link AuthResponse} with the user's details.
     */
    AuthResponse getCurrentUser(String email);
}