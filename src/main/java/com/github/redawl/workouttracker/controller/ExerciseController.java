package com.github.redawl.workouttracker.controller;

import com.github.redawl.workouttracker.model.data.Exercise;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

@Tag(name="Exercise", description = "Manage exercises")
public interface ExerciseController {
    @Operation(
            summary = "Add new exercise",
            responses = {
                    @ApiResponse(
                            description = "Ok - exercise was added successfully",
                            responseCode = "201",
                            content = @Content
                    ),
            }

    )
    void addExercise(Exercise exercise, HttpServletResponse response);

    @Operation(
            summary = "Retrieve exercise by name",
            responses = {
                    @ApiResponse(
                            description = "Ok - exercise was retrieved successfully",
                            responseCode = "200",
                            content = @Content
                    ),
                    @ApiResponse(
                            description = "Not Found - Exercise with that name does not exist",
                            responseCode = "404",
                            content = @Content
                    )
            }

    )
    Exercise getExerciseByName(String name, HttpServletResponse response);

    @Operation(
            summary = "Retrieve all existing exercises",
            responses = {
                    @ApiResponse(
                            description = "Ok - exercise were received successfully",
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = Exercise.class))
                            )
                    )
            }

    )
    List<Exercise> getExercises();
}
