package com.fitcare.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "roadmaps")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Roadmap {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private User.GoalType goalType;

    @Column(nullable = false)
    private String title;

    private String description;
    private String overview;
    private Integer totalWeeks;
    private String expectedResults;

    @Enumerated(EnumType.STRING)
    private User.FitnessLevel fitnessLevel;

    private boolean active = true;
    private LocalDateTime createdAt;


    @OneToMany(mappedBy = "roadmap",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    @OrderBy("phaseNumber ASC")
    @Builder.Default
    private Set<RoadmapPhase> phases =
            new LinkedHashSet<>();

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}