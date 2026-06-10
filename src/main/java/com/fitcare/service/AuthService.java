package com.fitcare.service;

import com.fitcare.dto.AuthDTO;
import com.fitcare.model.FitnessGoal;
import com.fitcare.model.User;
import com.fitcare.model.UserGoal;
import com.fitcare.repository.FitnessGoalRepository;
import com.fitcare.repository.UserGoalRepository;
import com.fitcare.repository.UserRepository;
import com.fitcare.security.JwtUtil;
import org.springframework.security.authentication
        .AuthenticationManager;
import org.springframework.security.authentication
        .UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password
        .PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation
        .Transactional;

import java.time.LocalDate;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager
            authenticationManager;

    // ✅ Add these two repositories
    private final FitnessGoalRepository
            fitnessGoalRepository;
    private final UserGoalRepository
            userGoalRepository;

    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtUtil jwtUtil,
            AuthenticationManager
                    authenticationManager,
            FitnessGoalRepository
                    fitnessGoalRepository,
            UserGoalRepository
                    userGoalRepository) {

        this.userRepository      = userRepository;
        this.passwordEncoder     = passwordEncoder;
        this.jwtUtil             = jwtUtil;
        this.authenticationManager =
                authenticationManager;
        this.fitnessGoalRepository =
                fitnessGoalRepository;
        this.userGoalRepository  =
                userGoalRepository;
    }

    // ── Register ──────────────────────────────
    public AuthDTO.AuthResponse register(
            AuthDTO.RegisterRequest request) {

        if (userRepository.existsByEmail(
                request.getEmail())) {
            throw new RuntimeException(
                    "Email already registered");
        }

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(
                        request.getPassword()))
                .gender(request.getGender())
                .dateOfBirth(
                        request.getDateOfBirth()
                                != null
                                ? LocalDate.parse(
                                request
                                        .getDateOfBirth())
                                : null)
                .heightCm(request.getHeightCm())
                .weightKg(request.getWeightKg())
                .fitnessLevel(
                        request.getFitnessLevel()
                                != null
                                ? User.FitnessLevel.valueOf(
                                request
                                        .getFitnessLevel()
                                        .toUpperCase())
                                : User.FitnessLevel
                                .BEGINNER)
                .role(User.Role.USER)
                .enabled(true)
                .build();

        userRepository.save(user);



        String token =
                jwtUtil.generateToken(user);

        return AuthDTO.AuthResponse.builder()
                .token(token)
                .type("Bearer")
                .userId(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole().name())
                .isNewUser(true)
                .build();
    }

    // ── Login ─────────────────────────────────
    public AuthDTO.AuthResponse login(
            AuthDTO.LoginRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()));

        User user = userRepository
                .findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new RuntimeException(
                                "User not found"));

        String token =
                jwtUtil.generateToken(user);

        return AuthDTO.AuthResponse.builder()
                .token(token)
                .type("Bearer")
                .userId(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole().name())
                .activeGoal(
                        user.getActiveGoal() != null
                                ? user.getActiveGoal()
                                .name()
                                : null)
                .isNewUser(false)
                .build();
    }

    // ── Guest Login ───────────────────────────
    @Transactional
    public AuthDTO.AuthResponse guestLogin() {

        String guestEmail =
                "guest_"
                        + System.currentTimeMillis()
                        + "@fitcare.com";

        // Create guest user
        User guest = User.builder()
                .name("Guest User")
                .email(guestEmail)
                .password(passwordEncoder.encode(
                        "guest123"))
                .gender("MALE")
                .heightCm(170.0)
                .weightKg(70.0)
                .fitnessLevel(
                        User.FitnessLevel.BEGINNER)
                .role(User.Role.GUEST)
                .enabled(true)
                .build();

        userRepository.save(guest);

        // Auto set Six Pack goal for guest
        FitnessGoal fitnessGoal =
                fitnessGoalRepository
                        .findByGoalTypeAndActiveTrue(
                                User.GoalType.SIX_PACK_ABS)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Goal not found"));

        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate
                .plusWeeks(
                        fitnessGoal.getTotalWeeks());

        // Create user goal with demo progress
        UserGoal userGoal = UserGoal.builder()
                .user(guest)
                .fitnessGoal(fitnessGoal)
                .status(UserGoal.GoalStatus.ACTIVE)
                .startDate(startDate)
                .expectedEndDate(endDate)
                .currentWeek(3)
                .completionPercentage(25)
                .startingWeightKg(70.0)
                .build();

        userGoalRepository.save(userGoal);

        // Update active goal
        guest.setActiveGoal(
                User.GoalType.SIX_PACK_ABS);
        userRepository.save(guest);

        String token =
                jwtUtil.generateToken(guest);

        return AuthDTO.AuthResponse.builder()
                .token(token)
                .type("Bearer")
                .userId(guest.getId())
                .name("Guest User")
                .email(guestEmail)
                .role("GUEST")
                .activeGoal("SIX_PACK_ABS")
                .isNewUser(true)
                .build();
    }
}