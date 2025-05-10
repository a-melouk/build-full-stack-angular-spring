package com.openclassrooms.mddapi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserDto {

  @NotBlank(message = "Email cannot be blank")
  @Email(message = "Email should be valid")
  @Size(max = 50, message = "Email must be at most 50 characters")
  private String email;

  @NotBlank(message = "First name cannot be blank")
  @Size(max = 20, message = "First name must be at most 20 characters")
  private String firstName;

  @NotBlank(message = "Last name cannot be blank")
  @Size(max = 20, message = "Last name must be at most 20 characters")
  private String lastName;
}