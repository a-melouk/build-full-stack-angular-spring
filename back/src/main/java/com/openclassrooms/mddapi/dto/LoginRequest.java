package com.openclassrooms.mddapi.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for user login requests.
 * This class encapsulates the credentials (email/username and password) required for a user to log in.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {
    /**
     * The user's email or username. Cannot be blank.
     */
    @NotBlank(message = "Email or username is required")
    private String emailOrUsername;

    /**
     * The user's password. Cannot be blank.
     */
    @NotBlank(message = "Password is required")
    private String password;
}