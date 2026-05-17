package com.fitcare.repository;

import com.fitcare.model.User;
import com.fitcare.model.UserGoal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserGoalRepository
        extends JpaRepository<UserGoal, Long> {

    // Get user's active goal
    Optional<UserGoal> findByUserIdAndStatus(
            Long userId,
            UserGoal.GoalStatus status);

    // Get all goals for a user
    List<UserGoal> findByUserIdOrderByCreatedAtDesc(
            Long userId);

    // Get active goal for user
    @Query("SELECT ug FROM UserGoal ug " +
            "WHERE ug.user.id = :userId " +
            "AND ug.status = 'ACTIVE'")
    Optional<UserGoal> findActiveGoalByUserId(
            @Param("userId") Long userId);

    // Count completed goals
    @Query("SELECT COUNT(ug) FROM UserGoal ug " +
            "WHERE ug.user.id = :userId " +
            "AND ug.status = 'COMPLETED'")
    long countCompletedGoals(
            @Param("userId") Long userId);

    // Check if user already has this goal active
    boolean existsByUserIdAndFitnessGoalIdAndStatus(
            Long userId,
            Long fitnessGoalId,
            UserGoal.GoalStatus status);
}