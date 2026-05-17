package com.fitcare.dto;

import jakarta.validation.constraints.*;
import lombok.*;

public class UserDTO {

    // ── User Profile Response ─────────────────
    @Getter @Setter
    @NoArgsConstructor @AllArgsConstructor
    @Builder
    public static class ProfileResponse {

        private Long id;
        private String name;
        private String email;
        private String gender;
        private String dateOfBirth;
        private Double heightCm;
        private Double weightKg;
        private Double bmi;
        private String bmiCategory;
        private String fitnessLevel;
        private String activeGoal;
        private Double targetWeightKg;
        private Integer currentStreak;
        private Integer longestStreak;
        private String lastActivityDate;
        private String createdAt;
    }

    // ── Update Profile Request ────────────────
    @Getter @Setter
    public static class UpdateRequest {

        @NotBlank(message = "Name is required")
        private String name;

        private String gender;
        private String dateOfBirth;

        @Positive(message =
                "Height must be positive")
        private Double heightCm;

        @Positive(message =
                "Weight must be positive")
        private Double weightKg;

        private Double targetWeightKg;
        private String fitnessLevel;
    }

    // ── Change Password Request ───────────────
    @Getter @Setter
    public static class ChangePasswordRequest {

        @NotBlank(message =
                "Old password is required")
        private String oldPassword;

        @NotBlank(message =
                "New password is required")
        @Size(min = 6,
                message = "Password must be " +
                        "at least 6 characters")
        private String newPassword;
    }

    // ── BMI Response ──────────────────────────
    @Getter @Setter
    @NoArgsConstructor @AllArgsConstructor
    @Builder
    public static class BmiResponse {

        private Double heightCm;
        private Double weightKg;
        private Double bmi;
        private String category;
        private String advice;
        private Double healthyWeightMin;
        private Double healthyWeightMax;
    }
}