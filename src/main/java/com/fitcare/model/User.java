package com.fitcare.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    private String gender;
    private LocalDate dateOfBirth;

    // ── Physical Stats ────────────────────────
    private Double heightCm;
    private Double weightKg;
    private Double targetWeightKg;

    // ── Fitness Level ─────────────────────────
    @Enumerated(EnumType.STRING)
    private FitnessLevel fitnessLevel;

    // ── Active Goal ───────────────────────────
    @Enumerated(EnumType.STRING)
    private GoalType activeGoal;

    // ── Streak ────────────────────────────────
    private Integer currentStreak;
    private Integer longestStreak;
    private LocalDate lastActivityDate;

    // ── Role ──────────────────────────────────
    @Enumerated(EnumType.STRING)
    private Role role;

    private boolean enabled = true;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt      = LocalDateTime.now();
        updatedAt      = LocalDateTime.now();
        currentStreak  = 0;
        longestStreak  = 0;
        if (role == null) role = Role.USER;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // ── UserDetails ───────────────────────────
    @Override
    public Collection<? extends GrantedAuthority>
    getAuthorities() {
        return List.of(new SimpleGrantedAuthority(
                "ROLE_" + role.name()));
    }

    @Override
    public String getUsername() { return email; }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // ── Enums ─────────────────────────────────
    public enum FitnessLevel {
        BEGINNER,       // Never worked out
        INTERMEDIATE,   // 6 months experience
        ADVANCED        // 2+ years experience
    }

    public enum GoalType {
        LOSE_FAT,
        GAIN_WEIGHT,
        SIX_PACK_ABS,
        BUILD_BICEPS,
        BUILD_MUSCLE,
        IMPROVE_ENDURANCE,
        GET_FLEXIBLE,
        MAINTAIN_FITNESS
    }

    public enum Role {
        USER, ADMIN
    }
}