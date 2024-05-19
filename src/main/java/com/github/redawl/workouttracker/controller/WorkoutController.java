package com.github.redawl.workouttracker.controller;

import com.github.redawl.workouttracker.model.data.Workout;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;

import java.time.LocalDate;
import java.util.List;

@Tag(name="Workout", description = "Manage workouts")
public interface WorkoutController {

    @Operation(
            summary = "Retrieve a workout by date",
            responses = {
                    @ApiResponse(
                            description = "Ok - Workout was found for given date",
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            implementation = Workout.class
                                    )
                            )
                    ),
                    @ApiResponse(
                            description = "Not Found - Workout was not found for given date",
                            responseCode = "404",
                            content = @Content
                    )
            }
    )
    Workout getWorkoutByDate(LocalDate date, HttpServletResponse response);

    @Operation(
            summary = "Remove a workout by date",
            responses = {
                    @ApiResponse(
                            description = "No Content - Workout was removed successfully",
                            responseCode = "204",
                            content = @Content
                    ),
                    @ApiResponse(
                            description = "Not Found - Workout was not found for specified date",
                            responseCode = "404",
                            content = @Content
                    ),
                    @ApiResponse(
                            description = "Bad Request - Date was missing or invalid",
                            responseCode = "400",
                            content = @Content
                    )
            }
    )
    void removeWorkoutByDate(LocalDate date, HttpServletResponse response);

    @Operation(
            summary = "Add a new workout",
            responses = {
                    @ApiResponse(
                            description = "Created - Workout was created successfully",
                            responseCode = "201",
                            content = @Content
                    ),
                    @ApiResponse(
                            description = "Bad Request - Workout already exists for specified date",
                            responseCode = "400",
                            content = @Content
                    )
            }
    )
    void addWorkout(Workout workout, HttpServletResponse response);

    @Operation(
            summary = "Update a workout by date",
            responses = {
                    @ApiResponse(
                            description = "Ok - Workout was successfully updated",
                            content = @Content
                    )
            }
    )
    void updateWorkout(Workout workout, HttpServletResponse response);

    @Operation(
            summary = "Retrieve all workouts",
            responses = {
                    @ApiResponse(
                            description = "Ok - All workouts were retrieved successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(
                                            schema = @Schema(
                                                    implementation = Workout.class
                                            )
                                    )
                            )
                    )
            }
    )
    List<Workout> getAllWorkouts();
}
