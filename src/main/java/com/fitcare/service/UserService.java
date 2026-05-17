package com.fitcare.service;

import com.fitcare.dto.UserDTO;
import com.fitcare.model.User;
import com.fitcare.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context
        .SecurityContextHolder;
import org.springframework.security.crypto.password
        .PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // ── Get Current User ──────────────────────
    public User getCurrentUser() {
        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();
        return userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException(
                                "User not found"));
    }

    // ── Get Profile ───────────────────────────
    public UserDTO.ProfileResponse getProfile() {
        User user = getCurrentUser();
        return mapToProfile(user);
    }

    // ── Update Profile ────────────────────────
    public UserDTO.ProfileResponse updateProfile(
            UserDTO.UpdateRequest request) {

        User user = getCurrentUser();

        user.setName(request.getName());

        if (request.getGender() != null) {
            user.setGender(request.getGender());
        }
        if (request.getDateOfBirth() != null) {
            user.setDateOfBirth(LocalDate.parse(
                    request.getDateOfBirth()));
        }
        if (request.getHeightCm() != null) {
            user.setHeightCm(request.getHeightCm());
        }
        if (request.getWeightKg() != null) {
            user.setWeightKg(request.getWeightKg());
        }
        if (request.getTargetWeightKg() != null) {
            user.setTargetWeightKg(
                    request.getTargetWeightKg());
        }
        if (request.getFitnessLevel() != null) {
            try {
                user.setFitnessLevel(
                        User.FitnessLevel.valueOf(
                                request.getFitnessLevel()
                                        .toUpperCase()));
            } catch (IllegalArgumentException e) {
                throw new RuntimeException(
                        "Invalid fitness level: "
                                + request.getFitnessLevel());
            }
        }

        userRepository.save(user);
        return mapToProfile(user);
    }

    // ── Change Password ───────────────────────
    public void changePassword(
            UserDTO.ChangePasswordRequest request) {

        User user = getCurrentUser();

        if (!passwordEncoder.matches(
                request.getOldPassword(),
                user.getPassword())) {
            throw new RuntimeException(
                    "Old password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(
                request.getNewPassword()));
        userRepository.save(user);
    }

    // ── Calculate BMI ─────────────────────────
    public UserDTO.BmiResponse calculateBmi() {
        User user = getCurrentUser();

        if (user.getWeightKg() == null
                || user.getHeightCm() == null) {
            throw new RuntimeException(
                    "Please update your height "
                            + "and weight first");
        }

        double heightM =
                user.getHeightCm() / 100.0;
        double bmi = Math.round(
                (user.getWeightKg()
                        / (heightM * heightM)) * 10.0)
                / 10.0;

        String category = getBmiCategory(bmi);
        String advice   = getBmiAdvice(bmi);

        // Healthy weight range
        double minWeight = Math.round(
                18.5 * heightM * heightM * 10.0)
                / 10.0;
        double maxWeight = Math.round(
                24.9 * heightM * heightM * 10.0)
                / 10.0;

        return UserDTO.BmiResponse.builder()
                .heightCm(user.getHeightCm())
                .weightKg(user.getWeightKg())
                .bmi(bmi)
                .category(category)
                .advice(advice)
                .healthyWeightMin(minWeight)
                .healthyWeightMax(maxWeight)
                .build();
    }

    // ── Map to Profile ────────────────────────
    public UserDTO.ProfileResponse mapToProfile(
            User user) {

        // Calculate BMI
        double bmi = 0;
        String bmiCategory = "";
        if (user.getWeightKg() != null
                && user.getHeightCm() != null) {
            double h = user.getHeightCm() / 100.0;
            bmi = Math.round(
                    (user.getWeightKg() / (h * h))
                            * 10.0) / 10.0;
            bmiCategory = getBmiCategory(bmi);
        }

        return UserDTO.ProfileResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .gender(user.getGender())
                .dateOfBirth(
                        user.getDateOfBirth() != null
                                ? user.getDateOfBirth()
                                .toString()
                                : null)
                .heightCm(user.getHeightCm())
                .weightKg(user.getWeightKg())
                .bmi(bmi > 0 ? bmi : null)
                .bmiCategory(bmiCategory)
                .fitnessLevel(
                        user.getFitnessLevel() != null
                                ? user.getFitnessLevel()
                                .name()
                                : null)
                .activeGoal(
                        user.getActiveGoal() != null
                                ? user.getActiveGoal().name()
                                : null)
                .targetWeightKg(
                        user.getTargetWeightKg())
                .currentStreak(
                        user.getCurrentStreak())
                .longestStreak(
                        user.getLongestStreak())
                .lastActivityDate(
                        user.getLastActivityDate() != null
                                ? user.getLastActivityDate()
                                .toString()
                                : null)
                .createdAt(
                        user.getCreatedAt() != null
                                ? user.getCreatedAt()
                                .toString()
                                : null)
                .build();
    }

    // ── BMI Helpers ───────────────────────────
    private String getBmiCategory(double bmi) {
        if (bmi < 18.5) return "Underweight";
        if (bmi < 25.0) return "Normal";
        if (bmi < 30.0) return "Overweight";
        return "Obese";
    }

    private String getBmiAdvice(double bmi) {
        if (bmi < 18.5)
            return "You are underweight. Focus on "
                    + "gaining healthy weight with "
                    + "nutritious food and strength "
                    + "training.";
        if (bmi < 25.0)
            return "Your BMI is normal. Keep "
                    + "maintaining your healthy "
                    + "lifestyle!";
        if (bmi < 30.0)
            return "You are slightly overweight. "
                    + "A combination of cardio and "
                    + "clean diet will help you reach "
                    + "a healthy weight.";
        return "Your BMI indicates obesity. "
                + "Please consult a doctor and start "
                + "a structured fitness program.";
    }
}