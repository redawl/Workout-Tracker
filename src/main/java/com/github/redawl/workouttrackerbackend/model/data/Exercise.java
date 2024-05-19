package com.github.redawl.workouttrackerbackend.model.data;

import com.github.redawl.workouttrackerbackend.model.dto.ExerciseDto;
import lombok.Data;

@Data
public class Exercise {
    private String name;
    private Integer lbs;
    private Integer reps;
    private Integer sets;

    public static Exercise fromDto(ExerciseDto dto){
        Exercise exercise = new Exercise();
        exercise.setName(dto.getName());
        exercise.setLbs(dto.getLbs());
        exercise.setReps(dto.getReps());
        exercise.setSets(dto.getSets());

        return exercise;
    }
}
