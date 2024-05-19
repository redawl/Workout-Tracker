package com.github.redawl.workouttrackerbackend.model.data;

import com.github.redawl.workouttrackerbackend.model.dto.ExerciseDto;
import com.github.redawl.workouttrackerbackend.model.dto.WorkoutDto;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class Workout {
    private LocalDate date;
    private List<Exercise> exercises;

    public static Workout fromDto(WorkoutDto dto){
        Workout workout = new Workout();
        workout.setDate(dto.getDate());
        workout.setExercises(new ArrayList<>());

        for(ExerciseDto exerciseDto: dto.getExerciseDtos()){
            workout.getExercises().add(Exercise.fromDto(exerciseDto));
        }

        return workout;
    }
}
