package com.fitcare.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.util.List;

public class GoalDTO {

    // ── Fitness Goal Response ─────────────────
    @Getter @Setter
    @NoArgsConstructor @AllArgsConstructor
    @Builder
    public static class GoalResponse {

        private Long id;
        private String goalType;
        private String goalName;
        private String goalDescription;
        private String goalIcon;
        private Integer totalWeeks;
        private String difficulty;
        private String category;
        private Double targetBodyFat;
        private String targetDescription;
        private String expectedResults;
    }

    // ── Set Goal Request ──────────────────────
    @Getter @Setter
    public static class SetGoalRequest {

        @NotNull(message = "Goal type is required")
        private String goalType;

        private Double startingWeightKg;
        private Double startingBodyFat;
        private String userNotes;
    }

    // ── User Goal Response ────────────────────
    @Getter @Setter
    @NoArgsConstructor @AllArgsConstructor
    @Builder
    public static class UserGoalResponse {

        private Long id;
        private String goalType;
        private String goalName;
        private String goalIcon;
        private String status;
        private Integer currentWeek;
        private Integer totalWeeks;
        private Integer completionPercentage;
        private String startDate;
        private String expectedEndDate;
        private Double startingWeight;
        private Double currentWeight;
        private Double weightChange;
        private String userNotes;

        // Current phase info
        private String currentPhaseName;
        private String currentPhaseDescription;
        private Integer currentPhaseNumber;
        private Integer totalPhases;
    }

    // ── Goal Progress Update ──────────────────
    @Getter @Setter
    public static class UpdateProgressRequest {

        @Min(value = 0,
                message = "Percentage must be 0-100")
        @Max(value = 100,
                message = "Percentage must be 0-100")
        private Integer completionPercentage;

        private String userNotes;
    }
}