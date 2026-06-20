package com.fitcare.config;

import com.fitcare.model.*;
import com.fitcare.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final FitnessGoalRepository
            fitnessGoalRepository;
    private final RoadmapRepository
            roadmapRepository;

    @Override
    @Transactional
    public void run(String... args) {

        if (fitnessGoalRepository.count() > 0) {
            System.out.println(
                    "✅ Data already seeded.");
            return;
        }

        System.out.println(
                "🌱 Seeding FitCare data...");
        seedGoals();
        seedRoadmaps();
        System.out.println(
                "✅ Seeding complete!");
    }

    private void seedGoals() {
        List<FitnessGoal> goals = List.of(

                FitnessGoal.builder()
                        .goalType(User.GoalType.LOSE_FAT)
                        .goalName("Lose Fat")
                        .goalDescription(
                                "Burn body fat and get lean")
                        .goalIcon("fire")
                        .totalWeeks(12)
                        .difficulty(
                                FitnessGoal.Difficulty.MODERATE)
                        .category(
                                FitnessGoal.GoalCategory
                                        .WEIGHT_LOSS)
                        .targetBodyFatPercentage(15.0)
                        .targetDescription(
                                "Reach 15% body fat")
                        .expectedResults(
                                "Lose 4-8kg in 12 weeks")
                        .active(true)
                        .build(),

                FitnessGoal.builder()
                        .goalType(
                                User.GoalType.GAIN_WEIGHT)
                        .goalName("Gain Weight")
                        .goalDescription(
                                "Build healthy muscle mass")
                        .goalIcon("lightning")
                        .totalWeeks(12)
                        .difficulty(
                                FitnessGoal.Difficulty.MODERATE)
                        .category(
                                FitnessGoal.GoalCategory
                                        .MUSCLE_GAIN)
                        .targetDescription(
                                "Gain 4-6kg of muscle")
                        .expectedResults(
                                "Gain 4-6kg in 12 weeks")
                        .active(true)
                        .build(),

                FitnessGoal.builder()
                        .goalType(
                                User.GoalType.SIX_PACK_ABS)
                        .goalName("Six Pack Abs")
                        .goalDescription(
                                "Get visible six pack abs")
                        .goalIcon("target")
                        .totalWeeks(16)
                        .difficulty(
                                FitnessGoal.Difficulty.HARD)
                        .category(
                                FitnessGoal.GoalCategory
                                        .BODY_COMPOSITION)
                        .targetBodyFatPercentage(10.0)
                        .targetDescription(
                                "Body fat below 12%")
                        .expectedResults(
                                "Visible abs in 16 weeks")
                        .active(true)
                        .build(),

                FitnessGoal.builder()
                        .goalType(
                                User.GoalType.BUILD_BICEPS)
                        .goalName("Build Biceps")
                        .goalDescription(
                                "Grow bigger biceps")
                        .goalIcon("muscle")
                        .totalWeeks(10)
                        .difficulty(
                                FitnessGoal.Difficulty.MODERATE)
                        .category(
                                FitnessGoal.GoalCategory
                                        .STRENGTH)
                        .targetDescription(
                                "Add 2-3cm to biceps")
                        .expectedResults(
                                "Bigger arms in 10 weeks")
                        .active(true)
                        .build(),

                FitnessGoal.builder()
                        .goalType(
                                User.GoalType.BUILD_MUSCLE)
                        .goalName("Build Muscle")
                        .goalDescription(
                                "Build overall muscle mass")
                        .goalIcon("trophy")
                        .totalWeeks(12)
                        .difficulty(
                                FitnessGoal.Difficulty.HARD)
                        .category(
                                FitnessGoal.GoalCategory
                                        .MUSCLE_GAIN)
                        .targetDescription(
                                "Gain muscle in all groups")
                        .expectedResults(
                                "Noticeable muscle gain "
                                        + "in 12 weeks")
                        .active(true)
                        .build(),

                FitnessGoal.builder()
                        .goalType(
                                User.GoalType.IMPROVE_ENDURANCE)
                        .goalName("Improve Endurance")
                        .goalDescription(
                                "Build cardio fitness "
                                        + "and stamina")
                        .goalIcon("heart")
                        .totalWeeks(8)
                        .difficulty(
                                FitnessGoal.Difficulty.MODERATE)
                        .category(
                                FitnessGoal.GoalCategory.CARDIO)
                        .targetDescription(
                                "Run 5km non-stop")
                        .expectedResults(
                                "Run 5km in 8 weeks")
                        .active(true)
                        .build(),

                FitnessGoal.builder()
                        .goalType(
                                User.GoalType.GET_FLEXIBLE)
                        .goalName("Get Flexible")
                        .goalDescription(
                                "Improve flexibility with yoga")
                        .goalIcon("yoga")
                        .totalWeeks(8)
                        .difficulty(
                                FitnessGoal.Difficulty.EASY)
                        .category(
                                FitnessGoal.GoalCategory
                                        .FLEXIBILITY)
                        .targetDescription(
                                "Touch toes and do splits")
                        .expectedResults(
                                "Full flexibility in 8 weeks")
                        .active(true)
                        .build()
        );

        fitnessGoalRepository.saveAll(goals);
        System.out.println(
                "✅ " + goals.size()
                        + " goals seeded!");
    }

    private void seedRoadmaps() {

        // ── Lose Fat ──────────────────────────
        Roadmap loseFat = Roadmap.builder()
                .goalType(User.GoalType.LOSE_FAT)
                .title("12 Week Fat Loss Roadmap")
                .description(
                        "Structured 12 week fat loss plan")
                .overview(
                        "Combines cardio, strength "
                                + "and clean eating")
                .totalWeeks(12)
                .expectedResults(
                        "Lose 4-8kg of body fat")
                .active(true)
                .build();

        RoadmapPhase lf1 = RoadmapPhase.builder()
                .roadmap(loseFat)
                .phaseNumber(1)
                .phaseName("Foundation")
                .phaseDescription(
                        "Build workout habit "
                                + "and clean up diet")
                .startWeek(1).endWeek(4)
                .focusArea("Habit Building")
                .milestone("Workout 4x per week")
                .icon("FOUNDATION")
                .build();

        lf1.getTasks().addAll(List.of(
                PhaseTask.builder()
                        .roadmapPhase(lf1)
                        .taskName("Start Cardio")
                        .taskDescription(
                                "20-30 min walk or jog daily")
                        .taskType(
                                PhaseTask.TaskType.CARDIO)
                        .orderIndex(1)
                        .required(true).build(),
                PhaseTask.builder()
                        .roadmapPhase(lf1)
                        .taskName("Clean Your Diet")
                        .taskDescription(
                                "Remove junk food and sugar")
                        .taskType(PhaseTask.TaskType.DIET)
                        .orderIndex(2)
                        .required(true).build(),
                PhaseTask.builder()
                        .roadmapPhase(lf1)
                        .taskName("Drink Water")
                        .taskDescription(
                                "3 liters of water daily")
                        .taskType(PhaseTask.TaskType.HABIT)
                        .orderIndex(3)
                        .required(true).build()
        ));

        RoadmapPhase lf2 = RoadmapPhase.builder()
                .roadmap(loseFat)
                .phaseNumber(2)
                .phaseName("Acceleration")
                .phaseDescription(
                        "Increase intensity "
                                + "and tighten diet")
                .startWeek(5).endWeek(8)
                .focusArea("Fat Burning")
                .milestone("Lose 2-3kg")
                .icon("ACCELERATION")
                .build();

        lf2.getTasks().addAll(List.of(
                PhaseTask.builder()
                        .roadmapPhase(lf2)
                        .taskName("Add HIIT")
                        .taskDescription(
                                "3x per week HIIT cardio")
                        .taskType(
                                PhaseTask.TaskType.CARDIO)
                        .orderIndex(1)
                        .required(true).build(),
                PhaseTask.builder()
                        .roadmapPhase(lf2)
                        .taskName("High Protein Diet")
                        .taskDescription(
                                "2g protein per kg daily")
                        .taskType(PhaseTask.TaskType.DIET)
                        .orderIndex(2)
                        .required(true).build()
        ));

        RoadmapPhase lf3 = RoadmapPhase.builder()
                .roadmap(loseFat)
                .phaseNumber(3)
                .phaseName("Definition")
                .phaseDescription(
                        "Final push to target")
                .startWeek(9).endWeek(12)
                .focusArea("Body Definition")
                .milestone("Reach target weight")
                .icon("DEFINITION")
                .build();

        lf3.getTasks().addAll(List.of(
                PhaseTask.builder()
                        .roadmapPhase(lf3)
                        .taskName("Peak Cardio")
                        .taskDescription(
                                "5x per week cardio")
                        .taskType(
                                PhaseTask.TaskType.CARDIO)
                        .orderIndex(1)
                        .required(true).build(),
                PhaseTask.builder()
                        .roadmapPhase(lf3)
                        .taskName("Strict Diet")
                        .taskDescription(
                                "Zero cheat meals")
                        .taskType(PhaseTask.TaskType.DIET)
                        .orderIndex(2)
                        .required(true).build()
        ));

        loseFat.getPhases().addAll(
                List.of(lf1, lf2, lf3));
        roadmapRepository.save(loseFat);

        // ── Six Pack ──────────────────────────
        Roadmap sixPack = Roadmap.builder()
                .goalType(User.GoalType.SIX_PACK_ABS)
                .title("16 Week Six Pack Roadmap")
                .description(
                        "Get visible six pack in 16 weeks")
                .overview(
                        "Core training, fat loss "
                                + "and strict diet")
                .totalWeeks(16)
                .expectedResults(
                        "Visible six pack abs")
                .active(true)
                .build();

        RoadmapPhase sp1 = RoadmapPhase.builder()
                .roadmap(sixPack)
                .phaseNumber(1)
                .phaseName("Fat Loss Phase")
                .phaseDescription(
                        "Reduce body fat to reveal abs")
                .startWeek(1).endWeek(6)
                .focusArea("Fat Loss")
                .milestone("Reach 15% body fat")
                .icon("FATBURN")
                .build();

        sp1.getTasks().addAll(List.of(
                PhaseTask.builder()
                        .roadmapPhase(sp1)
                        .taskName("Daily Cardio")
                        .taskDescription(
                                "30 min cardio every day")
                        .taskType(
                                PhaseTask.TaskType.CARDIO)
                        .orderIndex(1)
                        .required(true).build(),
                PhaseTask.builder()
                        .roadmapPhase(sp1)
                        .taskName("Zero Sugar")
                        .taskDescription(
                                "Eliminate all sugar")
                        .taskType(PhaseTask.TaskType.DIET)
                        .orderIndex(2)
                        .required(true).build()
        ));

        RoadmapPhase sp2 = RoadmapPhase.builder()
                .roadmap(sixPack)
                .phaseNumber(2)
                .phaseName("Core Building")
                .phaseDescription(
                        "Build strong core muscles")
                .startWeek(7).endWeek(12)
                .focusArea("Core Strength")
                .milestone("5 min plank hold")
                .icon("CORE")
                .build();

        sp2.getTasks().addAll(List.of(
                PhaseTask.builder()
                        .roadmapPhase(sp2)
                        .taskName("Core Daily")
                        .taskDescription(
                                "Plank, crunches, leg raises")
                        .taskType(
                                PhaseTask.TaskType.WORKOUT)
                        .orderIndex(1)
                        .required(true).build()
        ));

        RoadmapPhase sp3 = RoadmapPhase.builder()
                .roadmap(sixPack)
                .phaseNumber(3)
                .phaseName("Definition Phase")
                .phaseDescription(
                        "Final push for visible abs")
                .startWeek(13).endWeek(16)
                .focusArea("Ab Definition")
                .milestone("Visible six pack!")
                .icon("DEFINITION")
                .build();

        sp3.getTasks().addAll(List.of(
                PhaseTask.builder()
                        .roadmapPhase(sp3)
                        .taskName("Peak Training")
                        .taskDescription(
                                "2x daily training")
                        .taskType(
                                PhaseTask.TaskType.WORKOUT)
                        .orderIndex(1)
                        .required(true).build()
        ));

        sixPack.getPhases().addAll(
                List.of(sp1, sp2, sp3));
        roadmapRepository.save(sixPack);

        // ── Gain Weight ───────────────────────
        Roadmap gainWeight = Roadmap.builder()
                .goalType(User.GoalType.GAIN_WEIGHT)
                .title("12 Week Mass Gain Roadmap")
                .description(
                        "Build muscle and gain weight")
                .overview(
                        "Progressive overload with "
                                + "calorie surplus")
                .totalWeeks(12)
                .expectedResults(
                        "Gain 4-6kg muscle mass")
                .active(true)
                .build();

        RoadmapPhase gw1 = RoadmapPhase.builder()
                .roadmap(gainWeight)
                .phaseNumber(1)
                .phaseName("Foundation")
                .phaseDescription(
                        "Learn form and build habit")
                .startWeek(1).endWeek(4)
                .focusArea("Form and Habit")
                .milestone("Master basic lifts")
                .icon("FOUNDATION")
                .build();

        gw1.getTasks().addAll(List.of(
                PhaseTask.builder()
                        .roadmapPhase(gw1)
                        .taskName("Learn Basic Lifts")
                        .taskDescription(
                                "Squat, bench, deadlift form")
                        .taskType(
                                PhaseTask.TaskType.WORKOUT)
                        .orderIndex(1)
                        .required(true).build(),
                PhaseTask.builder()
                        .roadmapPhase(gw1)
                        .taskName("Calorie Surplus")
                        .taskDescription(
                                "Eat 300-500 cal above TDEE")
                        .taskType(PhaseTask.TaskType.DIET)
                        .orderIndex(2)
                        .required(true).build()
        ));

        RoadmapPhase gw2 = RoadmapPhase.builder()
                .roadmap(gainWeight)
                .phaseNumber(2)
                .phaseName("Growth Phase")
                .phaseDescription(
                        "Progressive overload training")
                .startWeek(5).endWeek(8)
                .focusArea("Muscle Growth")
                .milestone("Increase lifts by 10-20%")
                .icon("GROWTH")
                .build();

        gw2.getTasks().addAll(List.of(
                PhaseTask.builder()
                        .roadmapPhase(gw2)
                        .taskName("Progressive Overload")
                        .taskDescription(
                                "Add weight every workout")
                        .taskType(
                                PhaseTask.TaskType.WORKOUT)
                        .orderIndex(1)
                        .required(true).build()
        ));

        RoadmapPhase gw3 = RoadmapPhase.builder()
                .roadmap(gainWeight)
                .phaseNumber(3)
                .phaseName("Peak Phase")
                .phaseDescription(
                        "Maximum intensity training")
                .startWeek(9).endWeek(12)
                .focusArea("Peak Performance")
                .milestone("Reach target weight")
                .icon("PEAK")
                .build();

        gw3.getTasks().addAll(List.of(
                PhaseTask.builder()
                        .roadmapPhase(gw3)
                        .taskName("Heavy Compound Lifts")
                        .taskDescription(
                                "Heavy squats and deadlifts")
                        .taskType(
                                PhaseTask.TaskType.WORKOUT)
                        .orderIndex(1)
                        .required(true).build()
        ));

        gainWeight.getPhases().addAll(
                List.of(gw1, gw2, gw3));
        roadmapRepository.save(gainWeight);

        System.out.println(
                "✅ Roadmaps seeded!");
    }
}