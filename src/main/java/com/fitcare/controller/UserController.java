package com.fitcare.controller;

import com.fitcare.dto.ApiResponse;
import com.fitcare.dto.UserDTO;
import com.fitcare.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // ── GET /api/users/me ─────────────────────
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<
            UserDTO.ProfileResponse>> getProfile() {

        return ResponseEntity.ok(
                ApiResponse.success(
                        userService.getProfile()));
    }

    // ── PUT /api/users/me ─────────────────────
    @PutMapping("/me")
    public ResponseEntity<ApiResponse<
            UserDTO.ProfileResponse>> updateProfile(
            @Valid @RequestBody
            UserDTO.UpdateRequest request) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Profile updated!",
                        userService.updateProfile(
                                request)));
    }

    // ── POST /api/users/change-password ───────
    @PostMapping("/change-password")
    public ResponseEntity<ApiResponse<Void>>
    changePassword(
            @Valid @RequestBody
            UserDTO.ChangePasswordRequest request) {

        userService.changePassword(request);
        return ResponseEntity.ok(
                ApiResponse.success(
                        "Password changed "
                                + "successfully!", null));
    }

    // ── GET /api/users/bmi ────────────────────
    @GetMapping("/bmi")
    public ResponseEntity<ApiResponse<
            UserDTO.BmiResponse>> getBmi() {

        return ResponseEntity.ok(
                ApiResponse.success(
                        userService.calculateBmi()));
    }
}