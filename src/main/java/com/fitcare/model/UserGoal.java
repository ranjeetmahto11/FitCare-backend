package com.fitcare.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_goals")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class UserGoal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ── User ──────────────────────────────────
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",
            nullable = false)
    private User user;

    // ── Goal ──────────────────────────────────
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "fitness_goal_id",
            nullable = false)
    private FitnessGoal fitnessGoal;

    // ── Progress ──────────────────────────────
    @Enumerated(EnumType.STRING)
    private GoalStatus status;

    // Current week user is on (1 to totalWeeks)
    private Integer currentWeek;

    // Overall completion percentage (0-100)
    private Integer completionPercentage;

    // ── Dates ─────────────────────────────────
    private LocalDate startDate;
    private LocalDate expectedEndDate;
    private LocalDate completedDate;

    // ── Starting Stats ────────────────────────
    // Recorded when goal is started
    private Double startingWeightKg;
    private Double startingBodyFat;

    // ── Notes ─────────────────────────────────
    private String userNotes;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt            = LocalDateTime.now();
        updatedAt            = LocalDateTime.now();
        currentWeek          = 1;
        completionPercentage = 0;
        if (status == null) {
            status = GoalStatus.ACTIVE;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // ── Enum ──────────────────────────────────
    public enum GoalStatus {
        ACTIVE,     // Currently working on it
        PAUSED,     // Temporarily stopped
        COMPLETED,  // Goal achieved
        ABANDONED   // Gave up
    }
}