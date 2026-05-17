package com.fitcare.controller;

import com.fitcare.dto.ApiResponse;
import com.fitcare.dto.RoadmapDTO;
import com.fitcare.service.RoadmapService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/roadmap")
@RequiredArgsConstructor
public class RoadmapController {

    private final RoadmapService roadmapService;

    // ── GET /api/roadmap/my ───────────────────
    @GetMapping("/my")
    public ResponseEntity<ApiResponse<
    RoadmapDTO.RoadmapResponse>>
    getMyRoadmap() {

        return ResponseEntity.ok(
                ApiResponse.success(
                        roadmapService
                                .getMyRoadmap()));
    }

    // ── GET /api/roadmap/{goalType} ───────────
    @GetMapping("/{goalType}")
    public ResponseEntity<ApiResponse<
    RoadmapDTO.RoadmapResponse>>
    getRoadmapByGoal(
            @PathVariable String goalType) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        roadmapService
                                .getRoadmapByGoal(
                                        goalType)));
    }
}