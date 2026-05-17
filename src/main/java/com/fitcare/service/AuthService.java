package com.fitcare.service;

import com.fitcare.dto.AuthDTO.*;
import com.fitcare.model.User;
import com.fitcare.repository.UserRepository;
import com.fitcare.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authManager;

    public AuthResponse register(RegisterRequest req) {
        if (userRepository.existsByEmail(req.getEmail())) 
            throw new RuntimeException("Email already registered");

        User user = User.builder()
            .name(req.getName())
            .email(req.getEmail())
            .password(passwordEncoder.encode(req.getPassword()))
            .gender(req.getGender())
            .dateOfBirth(req.getDateOfBirth() != null ? LocalDate.parse(req.getDateOfBirth()) : null)
            .heightCm(req.getHeightCm())
            .weightKg(req.getWeightKg())
            .fitnessLevel(req.getFitnessLevel() != null ? User.FitnessLevel.valueOf(req.getFitnessLevel().toUpperCase()) : User.FitnessLevel.BEGINNER)
            .role(User.Role.USER)
            .enabled(true)
            .build();

        userRepository.save(user);
        return mapToResponse(user, true);
    }

    public AuthResponse login(LoginRequest req) {
        authManager.authenticate(new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword()));
        User user = userRepository.findByEmail(req.getEmail()).orElseThrow(() -> new RuntimeException("User not found"));
        return mapToResponse(user, false);
    }

    private AuthResponse mapToResponse(User user, boolean isNew) {
        return AuthResponse.builder()
            .token(jwtUtil.generateToken(user))
            .type("Bearer")
            .userId(user.getId())
            .name(user.getName())
            .email(user.getEmail())
            .role(user.getRole().name())
            .activeGoal(user.getActiveGoal() != null ? user.getActiveGoal().name() : null)
            .isNewUser(isNew)
            .build();
    }
}