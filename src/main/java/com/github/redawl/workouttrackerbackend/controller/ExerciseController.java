package com.github.redawl.workouttrackerbackend.controller;

import com.github.redawl.workouttrackerbackend.model.data.Exercise;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name="Exercise", description = "Manage exercises")
public interface ExerciseController {
    @Operation(
            summary = "Retrieve exercise information by name"
    )
    Exercise getExercise(String name);

    @Operation(
            summary = "Add new exercise"
    )
    void addExercise(Exercise exercise);
}
