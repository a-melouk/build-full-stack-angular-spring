package com.openclassrooms.mddapi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for updating user profile information.
 * This class contains fields for updating the user's email, username, and password.
 * Fields are optional and only provided values will be updated.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserUpdateDto {

    /**
     * The new email address for the user. Must be a valid email format.
     */
    @Email(message = "L'adresse email doit être valide")
    private String email;

    /**
     * The new username for the user. Must be between 3 and 20 characters.
     */
    @Size(min = 3, max = 20, message = "Le nom d'utilisateur doit contenir entre 3 et 20 caractères")
    private String username;

    /**
     * The new password for the user. Must be at least 6 characters long.
     */
    @Size(min = 6, message = "Le mot de passe doit contenir au moins 6 caractères")
    private String password;

    /**
     * Confirmation of the new password. This is used for validation and not persisted.
     */
    private String passwordConfirmation;
}