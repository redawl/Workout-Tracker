package com.github.redawl.workouttracker.model.data;

import com.github.redawl.workouttracker.model.dto.ExerciseDto;
import com.github.redawl.workouttracker.model.dto.ExerciseReferenceDto;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Comparator;
import java.util.Optional;

@Data
public class Exercise {
    @NotNull
    private String name;
    @NotNull
    private Integer lbs = 0;
    @NotNull
    private Integer reps = 0;
    @NotNull
    private Integer sets = 0;

    public static Exercise fromDto(ExerciseDto dto){
        Exercise exercise = new Exercise();
        exercise.setName(dto
                .getExerciseReference()
                .getName());
        exercise.setLbs(dto.getLbs());
        exercise.setReps(dto.getReps());
        exercise.setSets(dto.getSets());

        return exercise;
    }

    public static Exercise fromReferenceDto(ExerciseReferenceDto dto){
        Optional<Exercise> exercise = dto.getExercises().stream()
                .max(Comparator.comparing(x -> x.getWorkout().getDate()))
                .map(Exercise::fromDto);

        if(exercise.isPresent()){
            return exercise.get();
        }

        Exercise newExercise = new Exercise();
        newExercise.setName(dto.getName());
        return newExercise;
    }
}
