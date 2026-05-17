package com.fitcare.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "phase_tasks")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class PhaseTask {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "roadmap_phase_id",
            nullable = false)
    private RoadmapPhase roadmapPhase;

    // ── Task Details ──────────────────────────
    @Column(nullable = false)
    private String taskName;

    private String taskDescription;

    @Enumerated(EnumType.STRING)
    private TaskType taskType;

    private Integer orderIndex;

    // Is this task required or optional
    private boolean required = true;

    // ── Enum ──────────────────────────────────
    public enum TaskType {
        WORKOUT,
        DIET,
        HABIT,
        MEASUREMENT,
        REST,
        CARDIO,
        STRETCHING
    }
}