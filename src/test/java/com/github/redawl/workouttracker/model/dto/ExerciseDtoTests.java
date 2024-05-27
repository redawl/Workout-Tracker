package com.github.redawl.workouttracker.model.dto;


import com.github.redawl.workouttracker.model.data.Exercise;
import com.github.redawl.workouttracker.model.data.Workout;
import org.instancio.Instancio;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ExerciseDtoTests {
    @Test
    void exerciseDtoFromExerciseShouldResultInEqualFields(){
        Exercise exercise = Instancio.create(Exercise.class);
        String userJwt = Instancio.create(String.class);


        ExerciseDto dto = ExerciseDto.from(exercise, userJwt);

        Assertions.assertEquals(exercise.getLbs(), dto.getLbs());
        Assertions.assertEquals(exercise.getReps(), dto.getReps());
        Assertions.assertEquals(exercise.getSets(), dto.getSets());
    }
}
