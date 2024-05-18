package com.github.redawl.workouttrackerbackend.controller.impl;

import com.github.redawl.workouttrackerbackend.controller.WorkoutController;
import com.github.redawl.workouttrackerbackend.model.data.Workout;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/workout")
public class WorkoutControllerImpl implements WorkoutController {
    @Override
    @GetMapping
    public Workout getWorkoutByDate(@RequestParam LocalDate date) {
        return null;
    }

    @Override
    @DeleteMapping
    public void removeWorkoutByDate(@RequestParam LocalDate date) {

    }

    @Override
    @PostMapping
    public void addWorkout(@RequestBody Workout workout) {

    }

    @Override
    @PatchMapping
    public void updateWorkout(@RequestParam LocalDate date, @RequestBody Workout workout) {

    }
}
