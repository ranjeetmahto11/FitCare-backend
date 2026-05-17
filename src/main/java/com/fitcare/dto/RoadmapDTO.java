package com.fitcare.dto;

import lombok.*;
import java.util.List;

public class RoadmapDTO {

    // ── Full Roadmap Response ─────────────────
    @Getter @Setter
    @NoArgsConstructor @AllArgsConstructor
    @Builder
    public static class RoadmapResponse {

        private Long id;
        private String goalType;
        private String title;
        private String description;
        private String overview;
        private Integer totalWeeks;
        private String expectedResults;
        private String fitnessLevel;
        private List<PhaseResponse> phases;
    }

    // ── Phase Response ────────────────────────
    @Getter @Setter
    @NoArgsConstructor @AllArgsConstructor
    @Builder
    public static class PhaseResponse {

        private Long id;
        private Integer phaseNumber;
        private String phaseName;
        private String phaseDescription;
        private Integer startWeek;
        private Integer endWeek;
        private String focusArea;
        private String milestone;
        private String icon;
        private boolean completed;
        private boolean current;
        private List<TaskResponse> tasks;
    }

    // ── Task Response ─────────────────────────
    @Getter @Setter
    @NoArgsConstructor @AllArgsConstructor
    @Builder
    public static class TaskResponse {

        private Long id;
        private String taskName;
        private String taskDescription;
        private String taskType;
        private Integer orderIndex;
        private boolean required;
        private boolean completed;
    }
}