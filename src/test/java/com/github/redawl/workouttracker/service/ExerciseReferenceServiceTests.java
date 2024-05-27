package com.github.redawl.workouttracker.service;

import com.github.redawl.workouttracker.exception.NotFoundException;
import com.github.redawl.workouttracker.model.data.Exercise;
import com.github.redawl.workouttracker.model.dto.ExerciseReferenceDto;
import com.github.redawl.workouttracker.model.dto.UserDto;
import com.github.redawl.workouttracker.model.repository.ExerciseReferenceRepository;
import org.instancio.Instancio;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ExerciseReferenceServiceTests {
    @Mock
    ExerciseReferenceRepository exerciseReferenceRepository;

    @BeforeAll
    void setupTests(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getExerciseShouldThrowNotFoundForInvalidName(){
        String name = Instancio.create(String.class);
        String userJwt = Instancio.create(String.class);

        ExerciseReferenceService exerciseReferenceService = new ExerciseReferenceService(exerciseReferenceRepository);

        Assertions.assertThrows(NotFoundException.class, () -> exerciseReferenceService.getExerciseByName(name, userJwt));
    }

    @Test
    void getExerciseShouldReturnCorrectData() throws NotFoundException {
        ExerciseReferenceDto exercise = Instancio.create(ExerciseReferenceDto.class);
        // Manually set exercises with setter for backreferences
        exercise.setExercises(exercise.getExercises());
        String name = Instancio.create(String.class);
        String userJwt = Instancio.create(String.class);
        when(exerciseReferenceRepository.findByNameAndUser(name, UserDto.from(userJwt))).thenReturn(Optional.of(exercise));

        ExerciseReferenceService exerciseReferenceService = new ExerciseReferenceService(exerciseReferenceRepository);

        Exercise actual = exerciseReferenceService.getExerciseByName(name, userJwt);
        Assertions.assertEquals(actual, Exercise.fromReferenceDto(exercise));
    }

    @Test
    void getExercisesShouldReturnGeneratedExercises(){
        List<ExerciseReferenceDto> exerciseReferenceDtos = Instancio.createList(ExerciseReferenceDto.class);
        // Manually invoke setter for back references
        exerciseReferenceDtos.forEach(exerciseReferenceDto -> exerciseReferenceDto.setExercises(exerciseReferenceDto.getExercises()));
        String userJwt = Instancio.create(String.class);
        when(exerciseReferenceRepository.findAllByUser(UserDto.from(userJwt))).thenReturn(exerciseReferenceDtos);

        ExerciseReferenceService exerciseReferenceService = new ExerciseReferenceService(exerciseReferenceRepository);

        List<Exercise> actual = exerciseReferenceDtos.stream()
                .map(Exercise::fromReferenceDto).toList();
        Assertions.assertEquals(actual, exerciseReferenceService.getExercises(userJwt));
    }
}
