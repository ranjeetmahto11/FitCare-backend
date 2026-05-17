package com.fitcare.repository;

import com.fitcare.model.Roadmap;
import com.fitcare.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoadmapRepository
        extends JpaRepository<Roadmap, Long> {

    // Get roadmap by goal type
    Optional<Roadmap> findByGoalTypeAndActiveTrue(
            User.GoalType goalType);

    // Get roadmap by goal type and fitness level
    Optional<Roadmap> findByGoalTypeAndFitnessLevelAndActiveTrue(
            User.GoalType goalType,
            User.FitnessLevel fitnessLevel);

    // Get all active roadmaps
    List<Roadmap> findByActiveTrue();

    // Get roadmap with phases eagerly loaded
    @Query("SELECT r FROM Roadmap r " +
            "LEFT JOIN FETCH r.phases p " +
            "LEFT JOIN FETCH p.tasks " +
            "WHERE r.goalType = :goalType " +
            "AND r.active = true")
    Optional<Roadmap> findByGoalTypeWithPhases(
            @Param("goalType")
            User.GoalType goalType);
}