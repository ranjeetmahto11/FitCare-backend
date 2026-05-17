package com.fitcare.service;

import com.fitcare.dto.RoadmapDTO;
import com.fitcare.model.*;
import com.fitcare.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoadmapService {

    private final RoadmapRepository
            roadmapRepository;
    private final UserGoalRepository
            userGoalRepository;
    private final UserService userService;

    // ── Get Roadmap for My Goal ───────────────
    public RoadmapDTO.RoadmapResponse
    getMyRoadmap() {

        User user = userService.getCurrentUser();

        UserGoal userGoal = userGoalRepository
                .findActiveGoalByUserId(user.getId())
                .orElseThrow(() ->
                        new RuntimeException(
                                "No active goal. "
                                        + "Please set a goal."));

        User.GoalType goalType = userGoal
                .getFitnessGoal().getGoalType();
        int currentWeek =
                userGoal.getCurrentWeek() != null
                        ? userGoal.getCurrentWeek() : 1;

        Roadmap roadmap = roadmapRepository
                .findByGoalTypeWithPhases(goalType)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Roadmap not found "
                                        + "for goal: "
                                        + goalType));

        return mapToRoadmapResponse(
                roadmap, currentWeek);
    }

    // ── Get Roadmap by Goal Type ──────────────
    public RoadmapDTO.RoadmapResponse
    getRoadmapByGoal(String goalType) {

        Roadmap roadmap = roadmapRepository
                .findByGoalTypeWithPhases(
                        User.GoalType.valueOf(
                                goalType.toUpperCase()))
                .orElseThrow(() ->
                        new RuntimeException(
                                "Roadmap not found"));

        return mapToRoadmapResponse(roadmap, 1);
    }

    // ── Map to Roadmap Response ───────────────
    private RoadmapDTO.RoadmapResponse
    mapToRoadmapResponse(
            Roadmap roadmap,
            int currentWeek) {

        List<RoadmapDTO.PhaseResponse> phases =
                roadmap.getPhases().stream()
                        .map(phase -> {
                            boolean isCurrent =
                                    currentWeek >= phase
                                            .getStartWeek()
                                            && currentWeek <= phase
                                            .getEndWeek();
                            boolean isCompleted =
                                    currentWeek > phase
                                            .getEndWeek();

                            List<RoadmapDTO.TaskResponse>
                                    tasks = phase.getTasks()
                                    .stream()
                                    .map(t ->
                                            RoadmapDTO
                                                    .TaskResponse
                                                    .builder()
                                                    .id(t.getId())
                                                    .taskName(
                                                            t.getTaskName())
                                                    .taskDescription(
                                                            t.getTaskDescription())
                                                    .taskType(
                                                            t.getTaskType() != null
                                                                    ? t.getTaskType()
                                                                    .name()
                                                                    : null)
                                                    .orderIndex(
                                                            t.getOrderIndex())
                                                    .required(
                                                            t.isRequired())
                                                    .completed(
                                                            isCompleted)
                                                    .build())
                                    .collect(Collectors
                                            .toList());

                            return RoadmapDTO.PhaseResponse
                                    .builder()
                                    .id(phase.getId())
                                    .phaseNumber(
                                            phase.getPhaseNumber())
                                    .phaseName(
                                            phase.getPhaseName())
                                    .phaseDescription(
                                            phase.getPhaseDescription())
                                    .startWeek(
                                            phase.getStartWeek())
                                    .endWeek(
                                            phase.getEndWeek())
                                    .focusArea(
                                            phase.getFocusArea())
                                    .milestone(
                                            phase.getMilestone())
                                    .icon(phase.getIcon())
                                    .current(isCurrent)
                                    .completed(isCompleted)
                                    .tasks(tasks)
                                    .build();
                        })
                        .collect(Collectors.toList());

        return RoadmapDTO.RoadmapResponse.builder()
                .id(roadmap.getId())
                .goalType(
                        roadmap.getGoalType().name())
                .title(roadmap.getTitle())
                .description(
                        roadmap.getDescription())
                .overview(roadmap.getOverview())
                .totalWeeks(roadmap.getTotalWeeks())
                .expectedResults(
                        roadmap.getExpectedResults())
                .fitnessLevel(
                        roadmap.getFitnessLevel() != null
                                ? roadmap.getFitnessLevel()
                                .name()
                                : null)
                .phases(phases)
                .build();
    }
}