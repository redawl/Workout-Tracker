package com.github.redawl.workouttracker.model.dto;


import com.github.redawl.workouttracker.model.data.Exercise;
import org.instancio.Instancio;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ExerciseDtoTests {
    @Test
    void exerciseDtoFromExerciseShouldResultInEqualFields(){
        Exercise exercise = Instancio.create(Exercise.class);

        ExerciseDto dto = ExerciseDto.from(exercise);

        Assertions.assertEquals(exercise.getName(), dto.getName());
        Assertions.assertEquals(exercise.getLbs(), dto.getLbs());
        Assertions.assertEquals(exercise.getReps(), dto.getReps());
        Assertions.assertEquals(exercise.getSets(), dto.getSets());
    }
}