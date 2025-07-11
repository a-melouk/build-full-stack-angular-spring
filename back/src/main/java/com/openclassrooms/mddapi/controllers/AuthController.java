package com.openclassrooms.mddapi.controllers;

import com.openclassrooms.mddapi.dto.AuthResponse;
import com.openclassrooms.mddapi.dto.LoginRequest;
import com.openclassrooms.mddapi.dto.RegisterRequest;
import com.openclassrooms.mddapi.dto.UserUpdateDto;
import com.openclassrooms.mddapi.exception.EmailAlreadyExistsException;
import com.openclassrooms.mddapi.exception.UserNotFoundException;
import com.openclassrooms.mddapi.models.User;
import com.openclassrooms.mddapi.repository.UserRepository;
import com.openclassrooms.mddapi.security.JwtTokenProvider;
import com.openclassrooms.mddapi.services.AuthService;
import jakarta.servlet.http.Cookie;
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

/**
 * Controller for handling user authentication operations.
 * This includes registration, login, logout, and fetching user profile information.
 */
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

    @Autowired
    private AuthService authService;

    /**
     * Registers a new user account.
     *
     * @param registerRequest The request body containing registration details (email, username, password).
     * @param response The HTTP servlet response to set authentication cookies.
     * @return An {@link AuthResponse} containing the new user's details.
     * @throws EmailAlreadyExistsException if the email or username is already in use.
     */
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthResponse registerUser(@Valid @RequestBody RegisterRequest registerRequest,
            HttpServletResponse response) {
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new EmailAlreadyExistsException("Cette adresse email est déjà utilisée !");
        }
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            throw new EmailAlreadyExistsException("Ce nom d'utilisateur est déjà utilisé !");
        }
        logger.info("Attempting to register user with email: {} and username: {}",
                registerRequest.getEmail(), registerRequest.getUsername());

        User user = User.builder()
                .email(registerRequest.getEmail())
                .username(registerRequest.getUsername())
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

        // Set secure HTTP-only cookies
        setAuthCookies(response, jwt);

        return AuthResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .username(user.getUsernameField())
                .build();
    }

    /**
     * Authenticates a user and returns a JWT token.
     *
     * @param loginRequest The request body containing login credentials (email/username and password).
     * @param response The HTTP servlet response to set authentication cookies.
     * @return An {@link AuthResponse} containing the authenticated user's details.
     * @throws UserNotFoundException if the user cannot be found.
     */
    @PostMapping("/login")
    public AuthResponse authenticateUser(@Valid @RequestBody LoginRequest loginRequest, HttpServletResponse response) {
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

        User user = userRepository.findByEmailOrUsername(loginRequest.getEmailOrUsername())
                .orElseThrow(() -> new UserNotFoundException(
                        "Utilisateur non trouvé avec email ou nom d'utilisateur: " + loginRequest.getEmailOrUsername()));

        // Set secure HTTP-only cookies
        setAuthCookies(response, jwt);

        return AuthResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .username(user.getUsernameField())
                .build();
    }

    /**
     * Logs out the current user by clearing security context and authentication cookies.
     *
     * @param request The HTTP servlet request.
     * @param response The HTTP servlet response.
     * @return A response entity with an OK status.
     */
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        // Clear authentication cookies
        clearAuthCookies(response);

        // Clear security context
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }

        return ResponseEntity.ok().build();
    }

    /**
     * Retrieves the details of the currently authenticated user.
     *
     * @return A response entity containing the current user's details in an {@link AuthResponse}.
     */
    @GetMapping("/me")
    public ResponseEntity<AuthResponse> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        AuthResponse userInfo = authService.getCurrentUser(email);
        return ResponseEntity.ok(userInfo);
    }

    /**
     * Updates the profile of the currently authenticated user.
     *
     * @param userUpdateDto The request body containing the user details to update.
     * @return A response entity containing the updated user's details in an {@link AuthResponse}.
     */
    @PutMapping("/profile")
    public ResponseEntity<AuthResponse> updateProfile(@Valid @RequestBody UserUpdateDto userUpdateDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        AuthResponse updatedUser = authService.updateProfile(userUpdateDto, email);
        return ResponseEntity.ok(updatedUser);
    }

    /**
     * Sets authentication-related cookies in the HTTP response.
     *
     * @param response The HTTP servlet response.
     * @param jwt The JWT token to be stored in the cookies.
     */
    private void setAuthCookies(HttpServletResponse response, String jwt) {
        // Set access token cookie
        Cookie accessTokenCookie = new Cookie("accessToken", jwt);
        accessTokenCookie.setHttpOnly(true); // Prevent XSS
        accessTokenCookie.setSecure(false); // Set to true in production with HTTPS
        accessTokenCookie.setPath("/");
        accessTokenCookie.setMaxAge(24 * 60 * 60); // 24 hours
        accessTokenCookie.setAttribute("SameSite", "Lax"); // CSRF protection

        // Set token type cookie
        Cookie tokenTypeCookie = new Cookie("tokenType", "Bearer");
        tokenTypeCookie.setHttpOnly(true);
        tokenTypeCookie.setSecure(false); // Set to true in production with HTTPS
        tokenTypeCookie.setPath("/");
        tokenTypeCookie.setMaxAge(24 * 60 * 60); // 24 hours
        tokenTypeCookie.setAttribute("SameSite", "Lax");

        response.addCookie(accessTokenCookie);
        response.addCookie(tokenTypeCookie);
    }

    /**
     * Clears authentication-related cookies from the HTTP response.
     *
     * @param response The HTTP servlet response.
     */
    private void clearAuthCookies(HttpServletResponse response) {
        // Clear access token cookie
        Cookie accessTokenCookie = new Cookie("accessToken", "");
        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setSecure(false); // Set to true in production
        accessTokenCookie.setPath("/");
        accessTokenCookie.setMaxAge(0); // Expire immediately

        // Clear token type cookie
        Cookie tokenTypeCookie = new Cookie("tokenType", "");
        tokenTypeCookie.setHttpOnly(true);
        tokenTypeCookie.setSecure(false); // Set to true in production
        tokenTypeCookie.setPath("/");
        tokenTypeCookie.setMaxAge(0); // Expire immediately

        response.addCookie(accessTokenCookie);
        response.addCookie(tokenTypeCookie);
    }
}