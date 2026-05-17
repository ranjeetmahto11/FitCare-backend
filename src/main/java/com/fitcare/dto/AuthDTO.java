package com.fitcare.dto;

import jakarta.validation.constraints.*;
import lombok.*;

public class AuthDTO {

    // ── Register Request ──────────────────────
    @Getter @Setter
    public static class RegisterRequest {

        @NotBlank(message = "Name is required")
        private String name;

        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email format")
        private String email;

        @NotBlank(message = "Password is required")
        @Size(min = 6,
                message = "Password must be " +
                        "at least 6 characters")
        private String password;

        private String gender;
        private String dateOfBirth;

        @Positive(message =
                "Height must be positive")
        private Double heightCm;

        @Positive(message =
                "Weight must be positive")
        private Double weightKg;

        private String fitnessLevel;
    }

    // ── Login Request ─────────────────────────
    @Getter @Setter
    public static class LoginRequest {

        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email format")
        private String email;

        @NotBlank(message =
                "Password is required")
        private String password;
    }

    // ── Auth Response ─────────────────────────
    @Getter @Setter
    @NoArgsConstructor @AllArgsConstructor
    @Builder
    public static class AuthResponse {

        private String token;
        private String type = "Bearer";
        private Long userId;
        private String name;
        private String email;
        private String role;
        private String activeGoal;
        private boolean isNewUser;
    }
}