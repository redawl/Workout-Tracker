package com.github.redawl.workouttrackerbackend.controller;

import com.github.redawl.workouttrackerbackend.model.data.Workout;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

@Tag(name="Workouts", description = "Manage workouts")
public interface WorkoutController {

    @Operation(
            summary = "Retrieve a workout by date"
    )
    Workout getWorkoutByDate(@RequestParam LocalDate date);

    @Operation(
            summary = "Remove a workout by date"
    )
    void removeWorkoutByDate(@RequestParam LocalDate date);

    @Operation(
            summary = "Add a new workout"
    )
    void addWorkout(@RequestParam Workout workout);

    @Operation(
            summary = "Update a workout by date"
    )
    void updateWorkout(@RequestParam LocalDate date, @RequestParam Workout workout);
}
