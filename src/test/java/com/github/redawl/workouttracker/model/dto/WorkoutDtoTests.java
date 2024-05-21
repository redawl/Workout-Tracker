package com.github.redawl.workouttracker.model.dto;

import com.github.redawl.workouttracker.model.data.Workout;
import org.instancio.Instancio;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class WorkoutDtoTests {
    @Test
    void workoutDtofromWorkoutShouldHaveEqualDate(){
        Workout workout = Instancio.create(Workout.class);

        WorkoutDto dto = WorkoutDto.from(workout);

        Assertions.assertEquals(dto.getDate(), workout.getDate());
    }
}
