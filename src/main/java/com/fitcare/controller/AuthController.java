package com.fitcare.controller;

import com.fitcare.dto.AuthDTO;
import com.fitcare.dto.ApiResponse;
import com.fitcare.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/guest")
    public ResponseEntity<ApiResponse<
    AuthDTO.AuthResponse>> guestLogin() {

        AuthDTO.AuthResponse response =
                authService.guestLogin();

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Welcome Guest! "
                                + "Explore FitCare 👋",
                        response));
    }

    // ── POST /api/auth/register
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<
    AuthDTO.AuthResponse>> register(
            @Valid @RequestBody
            AuthDTO.RegisterRequest request) {

        AuthDTO.AuthResponse response =
                authService.register(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(
                        "Registration successful! "
                                + "Welcome to FitCare 💪",
                        response));
    }

    // ── POST /api/auth/login ──────────────────
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<
    AuthDTO.AuthResponse>> login(
            @Valid @RequestBody
            AuthDTO.LoginRequest request) {

        AuthDTO.AuthResponse response =
                authService.login(request);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Login successful! "
                                + "Let's crush your goals!",
                        response));
    }
}