package com.github.redawl.workouttracker.model.dto;

import com.github.redawl.workouttracker.model.data.Exercise;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ExerciseReferenceDtoTests {
    @Test
    void exerciseReferenceDtoFromExerciseShouldResultInEqualFields(){
        Exercise exercise = new Exercise();
        exercise.setName("testName");
        exercise.setLbs(40);
        exercise.setReps(8);
        exercise.setSets(4);

        ExerciseReferenceDto dto = ExerciseReferenceDto.fromExercise(exercise);
        ExerciseDto exerciseDto = ExerciseDto.from(exercise);

        Assertions.assertEquals(dto.getName(), exercise.getName());
        Assertions.assertEquals(dto.getExercise(), exerciseDto);
    }
}
