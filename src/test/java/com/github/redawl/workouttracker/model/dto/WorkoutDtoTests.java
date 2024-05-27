package com.github.redawl.workouttracker.model.dto;

import com.github.redawl.workouttracker.model.data.Workout;
import org.instancio.Instancio;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

class WorkoutDtoTests {
    @Test
    void workoutDtofromWorkoutShouldHaveEqualDate(){
        Workout workout = Instancio.create(Workout.class);
        String userJwt = Instancio.create(String.class);
        List<ExerciseReferenceDto> exerciseReferences = Instancio.createList(ExerciseReferenceDto.class);

        WorkoutDto dto = WorkoutDto.from(workout, userJwt, exerciseReferences);

        Assertions.assertEquals(dto.getDate(), workout.getDate());
    }
}
