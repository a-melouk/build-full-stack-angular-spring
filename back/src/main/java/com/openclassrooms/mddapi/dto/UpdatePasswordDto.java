package com.openclassrooms.mddapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for updating a user's password.
 * This class contains the old and new passwords, with validation constraints for the new password.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePasswordDto {

  /**
   * The user's current password. Cannot be blank.
   */
  @NotBlank(message = "Old password is required")
  private String oldPassword;

  /**
   * The new password for the user's account. It must be at least 8 characters long
   * and contain at least one digit, one lowercase letter, one uppercase letter, and one special character.
   */
  @NotBlank(message = "New password is required")
  @Size(min = 8, message = "Password must be at least 8 characters")
  @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).*$", message = "Password must contain at least one digit, one lowercase letter, one uppercase letter, and one special character")
  private String newPassword;
}