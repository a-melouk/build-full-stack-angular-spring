package com.openclassrooms.mddapi.config;

import com.openclassrooms.mddapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Configures the application's security components.
 * This class provides the necessary beans for user authentication and password encoding.
 */
@Configuration
@RequiredArgsConstructor

public class ApplicationConfig {

    private final UserRepository userRepository;

    /**
     * Provides a {@link UserDetailsService} bean that retrieves user details from the database.
     * The service uses either the email or username to find a user.
     *
     * @return a {@link UserDetailsService} implementation.
     */
    @Bean
    public UserDetailsService userDetailsService() {
        return emailOrUsername -> userRepository.findByEmailOrUsername(emailOrUsername)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email or username: " + emailOrUsername));
    }

    /**
     * Provides an {@link AuthenticationProvider} bean that uses the custom {@link UserDetailsService}
     * and {@link PasswordEncoder} for authentication.
     *
     * @return an {@link AuthenticationProvider} implementation.
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    /**
     * Provides the {@link AuthenticationManager} bean required for processing authentication requests.
     *
     * @param config the authentication configuration.
     * @return the {@link AuthenticationManager} instance.
     * @throws Exception if an error occurs while getting the authentication manager.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * Provides a {@link PasswordEncoder} bean that uses the BCrypt hashing algorithm for password encoding.
     *
     * @return a {@link PasswordEncoder} implementation.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}