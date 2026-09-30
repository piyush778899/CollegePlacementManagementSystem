package com.placement.service;

import com.placement.dto.AuthResponse;
import com.placement.dto.LoginRequest;
import com.placement.dto.RegisterRequest;
import com.placement.entity.Role;
import com.placement.entity.User;
import com.placement.repository.UserRepository;
import com.placement.security.JwtTokenProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtTokenProvider jwtTokenProvider,
                       AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.authenticationManager = authenticationManager;
    }

    /**
     * Register a new user.
     */
    public AuthResponse register(RegisterRequest request) {
        // Check if email is already taken
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email is already registered");
        }

        // Parse and validate role
        Role role;
        try {
            role = Role.valueOf(request.getRole().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                    "Invalid role. Must be one of: STUDENT, ADMIN, COMPANY");
        }

        // Create and save user
        User user = new User(
                request.getFullName(),
                request.getEmail(),
                passwordEncoder.encode(request.getPassword()),
                role
        );
        userRepository.save(user);

        // Generate JWT token
        String token = jwtTokenProvider.generateToken(user.getEmail());

        return new AuthResponse(
                token,
                user.getEmail(),
                user.getFullName(),
                user.getRole().name()
        );
    }

    /**
     * Authenticate a user and return a JWT token.
     */
    public AuthResponse login(LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );

            String token = jwtTokenProvider.generateToken(authentication);

            User user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new BadCredentialsException("Invalid credentials"));

            return new AuthResponse(
                    token,
                    user.getEmail(),
                    user.getFullName(),
                    user.getRole().name()
            );
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Invalid email or password");
        }
    }

    /**
     * Get current user info by email.
     */
    public AuthResponse getCurrentUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        return new AuthResponse(
                null,
                user.getEmail(),
                user.getFullName(),
                user.getRole().name()
        );
    }
}
