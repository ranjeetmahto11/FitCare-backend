package com.fitcare.controller;

import com.fitcare.dto.ApiResponse;
import com.fitcare.dto.GoalDTO.*;
import com.fitcare.service.GoalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/goals")
@RequiredArgsConstructor
public class GoalController {
    private final GoalService goalService;

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<GoalResponse>>> getAllGoals() {
        return ResponseEntity.ok(ApiResponse.success(goalService.getAllGoals()));
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<ApiResponse<GoalResponse>> getGoalByType(@PathVariable String type) {
        return ResponseEntity.ok(ApiResponse.success(goalService.getGoalByType(type)));
    }

    @PostMapping("/set")
    public ResponseEntity<ApiResponse<UserGoalResponse>> setGoal(@Valid @RequestBody SetGoalRequest req) {
        return ResponseEntity.ok(ApiResponse.success("Goal set! 🚀", goalService.setGoal(req)));
    }

    @GetMapping("/my")
    public ResponseEntity<ApiResponse<UserGoalResponse>> getMyGoal() {
        return ResponseEntity.ok(ApiResponse.success(goalService.getMyGoal()));
    }

    @GetMapping("/history")
    public ResponseEntity<ApiResponse<List<UserGoalResponse>>> getGoalHistory() {
        return ResponseEntity.ok(ApiResponse.success(goalService.getMyGoalHistory()));
    }

    @PostMapping("/advance-week")
    public ResponseEntity<ApiResponse<UserGoalResponse>> advanceWeek() {
        return ResponseEntity.ok(ApiResponse.success("Advanced to next week! 💪", goalService.advanceWeek()));
    }
}