package com.fitcare.service;

import com.fitcare.dto.GoalDTO;
import com.fitcare.model.*;
import com.fitcare.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation
        .Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GoalService {

    private final FitnessGoalRepository
            fitnessGoalRepository;
    private final UserGoalRepository
            userGoalRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    // ── Get All Goals ─────────────────────────
    public List<GoalDTO.GoalResponse>
    getAllGoals() {
        return fitnessGoalRepository
                .findByActiveTrue()
                .stream()
                .map(this::mapToGoalResponse)
                .collect(Collectors.toList());
    }

    // ── Get Goal by Type ──────────────────────
    public GoalDTO.GoalResponse getGoalByType(
            String goalType) {

        FitnessGoal goal = fitnessGoalRepository
                .findByGoalTypeAndActiveTrue(
                        User.GoalType.valueOf(
                                goalType
                                        .toUpperCase()))
                .orElseThrow(() ->
                        new RuntimeException(
                                "Goal not found"));
        return mapToGoalResponse(goal);
    }

    // ── Set User Goal ─────────────────────────
    @Transactional
    public GoalDTO.UserGoalResponse setGoal(
            GoalDTO.SetGoalRequest request) {

        User user = userService.getCurrentUser();

        // Pause existing active goal
        userGoalRepository
                .findActiveGoalByUserId(
                        user.getId())
                .ifPresent(existing -> {
                    existing.setStatus(
                            UserGoal.GoalStatus
                                    .PAUSED);
                    userGoalRepository
                            .save(existing);
                });

        // Find fitness goal
        FitnessGoal fitnessGoal =
                fitnessGoalRepository
                        .findByGoalTypeAndActiveTrue(
                                User.GoalType.valueOf(
                                        request.getGoalType()
                                                .toUpperCase()))
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Goal not found"));

        // Calculate dates
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate
                .plusWeeks(
                        fitnessGoal.getTotalWeeks());

        // Create user goal
        UserGoal userGoal = UserGoal.builder()
                .user(user)
                .fitnessGoal(fitnessGoal)
                .status(UserGoal.GoalStatus.ACTIVE)
                .startDate(startDate)
                .expectedEndDate(endDate)
                .currentWeek(1)
                .completionPercentage(0)
                .startingWeightKg(
                        request.getStartingWeightKg()
                                != null
                                ? request.getStartingWeightKg()
                                : user.getWeightKg())
                .startingBodyFat(
                        request.getStartingBodyFat())
                .userNotes(request.getUserNotes())
                .build();

        userGoalRepository.save(userGoal);

        // Update active goal on user
        user.setActiveGoal(
                User.GoalType.valueOf(
                        request.getGoalType()
                                .toUpperCase()));
        userRepository.save(user);

        return mapToUserGoalResponse(userGoal);
    }

    // ── Get My Active Goal ────────────────────
    public GoalDTO.UserGoalResponse getMyGoal() {

        User user = userService.getCurrentUser();

        UserGoal userGoal = userGoalRepository
                .findActiveGoalByUserId(
                        user.getId())
                .orElseThrow(() ->
                        new RuntimeException(
                                "No active goal. "
                                        + "Please set a "
                                        + "goal first."));

        return mapToUserGoalResponse(userGoal);
    }

    // ── Get Goal History ──────────────────────
    public List<GoalDTO.UserGoalResponse>
    getMyGoalHistory() {

        User user = userService.getCurrentUser();
        return userGoalRepository
                .findByUserIdOrderByCreatedAtDesc(
                        user.getId())
                .stream()
                .map(this::mapToUserGoalResponse)
                .collect(Collectors.toList());
    }

    // ── Advance Week ──────────────────────────
    @Transactional
    public GoalDTO.UserGoalResponse advanceWeek() {

        User user = userService.getCurrentUser();

        UserGoal userGoal = userGoalRepository
                .findActiveGoalByUserId(
                        user.getId())
                .orElseThrow(() ->
                        new RuntimeException(
                                "No active goal"));

        int nextWeek =
                userGoal.getCurrentWeek() + 1;
        int totalWeeks = userGoal
                .getFitnessGoal()
                .getTotalWeeks();

        if (nextWeek > totalWeeks) {
            userGoal.setStatus(
                    UserGoal.GoalStatus.COMPLETED);
            userGoal.setCompletedDate(
                    LocalDate.now());
            userGoal.setCompletionPercentage(100);
        } else {
            userGoal.setCurrentWeek(nextWeek);
            int pct = Math.round(
                    ((float) nextWeek
                            / totalWeeks) * 100);
            userGoal.setCompletionPercentage(pct);
        }

        userGoalRepository.save(userGoal);
        return mapToUserGoalResponse(userGoal);
    }

    // ── Map Goal Response ─────────────────────
    private GoalDTO.GoalResponse
    mapToGoalResponse(FitnessGoal goal) {

        return GoalDTO.GoalResponse.builder()
                .id(goal.getId())
                .goalType(
                        goal.getGoalType().name())
                .goalName(goal.getGoalName())
                .goalDescription(
                        goal.getGoalDescription())
                .goalIcon(goal.getGoalIcon())
                .totalWeeks(goal.getTotalWeeks())
                .difficulty(
                        goal.getDifficulty() != null
                                ? goal.getDifficulty()
                                .name()
                                : null)
                .category(
                        goal.getCategory() != null
                                ? goal.getCategory()
                                .name()
                                : null)
                .targetBodyFat(
                        goal.getTargetBodyFatPercentage())
                .targetDescription(
                        goal.getTargetDescription())
                .expectedResults(
                        goal.getExpectedResults())
                .build();
    }

    // ── Map User Goal Response ────────────────
    private GoalDTO.UserGoalResponse
    mapToUserGoalResponse(UserGoal ug) {

        FitnessGoal fg = ug.getFitnessGoal();

        return GoalDTO.UserGoalResponse.builder()
                .id(ug.getId())
                .goalType(
                        fg.getGoalType().name())
                .goalName(fg.getGoalName())
                .goalIcon(fg.getGoalIcon())
                .status(ug.getStatus().name())
                .currentWeek(
                        ug.getCurrentWeek())
                .totalWeeks(
                        fg.getTotalWeeks())
                .completionPercentage(
                        ug.getCompletionPercentage())
                .startDate(
                        ug.getStartDate() != null
                                ? ug.getStartDate()
                                .toString()
                                : null)
                .expectedEndDate(
                        ug.getExpectedEndDate()
                                != null
                                ? ug.getExpectedEndDate()
                                .toString()
                                : null)
                .startingWeight(
                        ug.getStartingWeightKg())
                .userNotes(ug.getUserNotes())
                .build();
    }
}