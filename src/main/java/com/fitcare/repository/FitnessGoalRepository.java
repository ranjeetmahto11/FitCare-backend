package com.fitcare.repository;

import com.fitcare.model.FitnessGoal;
import com.fitcare.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FitnessGoalRepository
        extends JpaRepository<FitnessGoal, Long> {

    // Get all active goals
    List<FitnessGoal> findByActiveTrue();

    // Find by goal type
    Optional<FitnessGoal> findByGoalTypeAndActiveTrue(
            User.GoalType goalType);

    // Find by category
    List<FitnessGoal> findByCategoryAndActiveTrue(
            FitnessGoal.GoalCategory category);

    // Find by difficulty
    List<FitnessGoal> findByDifficultyAndActiveTrue(
            FitnessGoal.Difficulty difficulty);
}