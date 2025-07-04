package com.openclassrooms.mddapi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for updating a user's profile information.
 * This class contains the fields that can be updated by a user.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserDto {

  /**
   * The user's new email address. It must be a valid email format and cannot be blank.
   */
  @NotBlank(message = "Email cannot be blank")
  @Email(message = "Email should be valid")
  @Size(max = 50, message = "Email must be at most 50 characters")
  private String email;

}