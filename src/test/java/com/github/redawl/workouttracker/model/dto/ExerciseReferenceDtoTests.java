package com.github.redawl.workouttracker.model.dto;

import com.github.redawl.workouttracker.model.data.Exercise;
import org.instancio.Instancio;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ExerciseReferenceDtoTests {
    @Test
    void exerciseReferenceDtoFromExerciseShouldResultInEqualFields(){
        Exercise exercise = Instancio.create(Exercise.class);
        String userJwt = Instancio.create(String.class);

        ExerciseReferenceDto dto = ExerciseReferenceDto.fromExercise(exercise, userJwt);
        ExerciseDto exerciseDto = ExerciseDto.from(exercise, userJwt);

        Assertions.assertEquals(dto.getName(), exercise.getName());
        Assertions.assertEquals(dto.getExercises().get(0), exerciseDto);
    }
}
