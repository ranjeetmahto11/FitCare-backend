package com.fitcare.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "fitness_goals")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class FitnessGoal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ── Goal Type ─────────────────────────────
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private User.GoalType goalType;

    @Column(nullable = false)
    private String goalName;

    private String goalDescription;
    private String goalIcon;

    // ── Duration ──────────────────────────────
    // Total weeks to complete this goal
    private Integer totalWeeks;

    // ── Target Values ─────────────────────────
    private Double targetBodyFatPercentage;
    private Double targetWeightKg;
    private String targetDescription;

    private String expectedResults;

    // ── Difficulty ────────────────────────────
    @Enumerated(EnumType.STRING)
    private Difficulty difficulty;

    // ── Category ──────────────────────────────
    @Enumerated(EnumType.STRING)
    private GoalCategory category;

    private boolean active = true;
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    // ── Enums ─────────────────────────────────
    public enum Difficulty {
        EASY,
        MODERATE,
        HARD,
        VERY_HARD
    }

    public enum GoalCategory {
        WEIGHT_LOSS,
        MUSCLE_GAIN,
        BODY_COMPOSITION,
        STRENGTH,
        CARDIO,
        FLEXIBILITY
    }
}