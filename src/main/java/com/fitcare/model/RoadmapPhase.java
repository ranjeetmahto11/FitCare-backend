package com.fitcare.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "roadmap_phases")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class RoadmapPhase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "roadmap_id",
            nullable = false)
    private Roadmap roadmap;

    @Column(nullable = false)
    private Integer phaseNumber;

    @Column(nullable = false)
    private String phaseName;

    private String phaseDescription;
    private Integer startWeek;
    private Integer endWeek;
    private String focusArea;
    private String milestone;
    private String icon;


    @OneToMany(mappedBy = "roadmapPhase",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    @OrderBy("orderIndex ASC")
    @Builder.Default
    private Set<PhaseTask> tasks =
            new LinkedHashSet<>();
}