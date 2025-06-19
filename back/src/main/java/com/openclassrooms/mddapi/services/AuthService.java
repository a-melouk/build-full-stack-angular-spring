package com.openclassrooms.mddapi.services;

import com.openclassrooms.mddapi.dto.UserUpdateDto;
import com.openclassrooms.mddapi.dto.AuthResponse;

public interface AuthService {
    AuthResponse updateProfile(UserUpdateDto userUpdateDto, String currentUserEmail);
    AuthResponse getCurrentUser(String email);
}