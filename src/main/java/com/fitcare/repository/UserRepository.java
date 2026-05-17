package com.fitcare.repository;

import com.fitcare.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository
        extends JpaRepository<User, Long> {

    // Find by email (login)
    Optional<User> findByEmail(String email);

    // Check email exists
    boolean existsByEmail(String email);

    // Find by active goal
    List<User> findByActiveGoal(
            User.GoalType goalType);

    // Find users with streak
    @Query("SELECT u FROM User u " +
            "WHERE u.currentStreak >= :days")
    List<User> findUsersWithStreakAtLeast(
            @Param("days") int days);

    // Find users who checked in today
    @Query("SELECT u FROM User u " +
            "WHERE u.lastActivityDate = :date")
    List<User> findUsersActiveToday(
            @Param("date") LocalDate date);
}