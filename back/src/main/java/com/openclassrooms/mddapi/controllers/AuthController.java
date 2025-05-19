package com.openclassrooms.mddapi.controllers;

import com.openclassrooms.mddapi.dto.AuthResponse;
import com.openclassrooms.mddapi.dto.LoginRequest;
import com.openclassrooms.mddapi.dto.RegisterRequest;
import com.openclassrooms.mddapi.exception.EmailAlreadyExistsException;
import com.openclassrooms.mddapi.exception.UserNotFoundException;
import com.openclassrooms.mddapi.models.User;
import com.openclassrooms.mddapi.repository.UserRepository;
import com.openclassrooms.mddapi.security.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthResponse registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new EmailAlreadyExistsException("Email is already taken!");
        }
        logger.info("Attempting to register user with email: {}", registerRequest.getEmail());

        User user = User.builder()
                .email(registerRequest.getEmail())
                .firstName(registerRequest.getFirstName())
                .lastName(registerRequest.getLastName())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .build();

        userRepository.save(user);
        logger.info("User {} registered successfully. Attempting authentication.", registerRequest.getEmail());

        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            registerRequest.getEmail(),
                            registerRequest.getPassword()));
            logger.info("Authentication successful for registered user: {}", registerRequest.getEmail());
        } catch (Exception e) {
            logger.error("Authentication failed for user {} after registration: ", registerRequest.getEmail(), e);
            throw e; // Re-throw to be caught by GlobalExceptionHandler or specific handlers
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt;
        try {
            jwt = tokenProvider.generateToken(authentication);
            logger.info("JWT token generated successfully for user: {}", registerRequest.getEmail());
        } catch (Exception e) {
            logger.error("JWT token generation failed for user {}: ", registerRequest.getEmail(), e);
            throw e; // Re-throw
        }

        return AuthResponse.builder()
                .token(jwt)
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .build();
    }

    @PostMapping("/login")
    public AuthResponse authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        logger.info("Attempting to authenticate user: {}", loginRequest.getEmailOrUsername());
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmailOrUsername(),
                            loginRequest.getPassword()));
            logger.info("Authentication successful for user: {}", loginRequest.getEmailOrUsername());
        } catch (Exception e) {
            logger.error("Authentication failed for user {}: ", loginRequest.getEmailOrUsername(), e);
            throw e; // Re-throw
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt;
        try {
            jwt = tokenProvider.generateToken(authentication);
            logger.info("JWT token generated successfully for user: {}", loginRequest.getEmailOrUsername());
        } catch (Exception e) {
            logger.error("JWT token generation failed for user {}: ", loginRequest.getEmailOrUsername(), e);
            throw e; // Re-throw
        }

        User user = userRepository.findByEmail(loginRequest.getEmailOrUsername())
                .orElseThrow(() -> new UserNotFoundException(
                        "User not found with email: " + loginRequest.getEmailOrUsername()));

        return AuthResponse.builder()
                .token(jwt)
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .build();
    }
}