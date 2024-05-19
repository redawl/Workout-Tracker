package com.github.redawl.workouttracker.model.data;

import com.github.redawl.workouttracker.model.dto.ExerciseDto;
import com.github.redawl.workouttracker.model.dto.ExerciseReferenceDto;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class Exercise {
    @NotNull
    private String name;
    @NotNull
    private Integer lbs;
    @NotNull
    private Integer reps;
    @NotNull
    private Integer sets;

    public static Exercise fromDto(ExerciseDto dto){
        Exercise exercise = new Exercise();
        exercise.setName(dto.getName());
        exercise.setLbs(dto.getLbs());
        exercise.setReps(dto.getReps());
        exercise.setSets(dto.getSets());

        return exercise;
    }

    public static Exercise fromReferenceDto(ExerciseReferenceDto dto){
        return fromDto(dto.getExercise());
    }
}
