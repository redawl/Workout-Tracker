package com.github.redawl.workouttracker.model.data;
import com.github.redawl.workouttracker.model.dto.WorkoutDto;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

@Data
public class Workout {
    @NotNull
    private LocalDate date;
    @NotNull
    private Set<Exercise> exercises;

    public static Workout fromDto(WorkoutDto dto){
        Workout workout = new Workout();
        workout.setDate(dto.getDate());

        workout.setExercises(dto.getExercises().stream().map(Exercise::fromDto).collect(Collectors.toSet()));

        return workout;
    }
}
