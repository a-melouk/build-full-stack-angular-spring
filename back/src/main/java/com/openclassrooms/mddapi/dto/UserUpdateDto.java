package com.openclassrooms.mddapi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserUpdateDto {

    @Email(message = "L'adresse email doit être valide")
    private String email;

    @Size(min = 3, max = 20, message = "Le nom d'utilisateur doit contenir entre 3 et 20 caractères")
    private String username;

    @Size(min = 6, message = "Le mot de passe doit contenir au moins 6 caractères")
    private String password;

    private String passwordConfirmation;
}