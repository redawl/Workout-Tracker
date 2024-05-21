package com.github.redawl.workouttracker.service;

import com.github.redawl.workouttracker.exception.ExistsException;
import com.github.redawl.workouttracker.exception.NotFoundException;
import com.github.redawl.workouttracker.model.data.Workout;
import com.github.redawl.workouttracker.model.dto.WorkoutDto;
import com.github.redawl.workouttracker.model.repository.WorkoutRepository;
import org.instancio.Instancio;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class WorkoutServiceTests {
    @Mock
    WorkoutRepository workoutRepository;

    @BeforeAll
    void setupTests(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getWorkoutWithBadDateShouldThrow(){
        LocalDate date = Instancio.create(LocalDate.class);

        WorkoutService workoutService = new WorkoutService(workoutRepository);

        Assertions.assertThrows(NotFoundException.class, () -> workoutService.getWorkoutByDate(date));
    }

    @Test
    void getWorkoutShouldReturnValid() throws NotFoundException {
        WorkoutDto workout = Instancio.create(WorkoutDto.class);
        LocalDate date = workout.getDate();
        when(workoutRepository.findById(date)).thenReturn(Optional.of(workout));

        WorkoutService workoutService = new WorkoutService(workoutRepository);

        Workout expected = Workout.fromDto(workout);
        Workout actual   = workoutService.getWorkoutByDate(date);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void getWorkoutsShouldReturnExpected(){
        List<WorkoutDto> workoutDtos = Instancio.createList(WorkoutDto.class);
        when(workoutRepository.findAll()).thenReturn(workoutDtos);

        WorkoutService workoutService = new WorkoutService(workoutRepository);

        List<Workout> expected = workoutDtos.stream()
                .map(Workout::fromDto)
                .toList();
        List<Workout> actual   = workoutService.getWorkouts();

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void addWorkoutShouldThrowIfDateAlreadyExists(){
        Workout workout = Instancio.create(Workout.class);
        when(workoutRepository.existsById(workout.getDate())).thenReturn(true);

        WorkoutService workoutService = new WorkoutService(workoutRepository);

        Assertions.assertThrows(ExistsException.class, () -> workoutService.addWorkout(workout));
    }

    @Test
    void addWorkoutShouldSucceedIfWorkoutDoesNotExist(){
        Workout workout = Instancio.create(Workout.class);
        when(workoutRepository.existsById(workout.getDate())).thenReturn(false);

        WorkoutService workoutService = new WorkoutService(workoutRepository);

        Assertions.assertDoesNotThrow(() -> workoutService.addWorkout(workout));
    }

    @Test
    void removeWorkoutShouldThrowIfWorkoutDoesNotExist(){
        LocalDate date = Instancio.create(LocalDate.class);

        WorkoutService workoutService = new WorkoutService(workoutRepository);

        Assertions.assertThrows(NotFoundException.class, () -> workoutService.removeWorkoutByDate(date));
    }

    @Test
    void removeWorkoutShouldSucceedIfWorkoutExists(){
        WorkoutDto workoutDto = Instancio.create(WorkoutDto.class);
        when(workoutRepository.findById(workoutDto.getDate())).thenReturn(Optional.of(workoutDto));

        WorkoutService workoutService = new WorkoutService(workoutRepository);

        Assertions.assertDoesNotThrow(() -> workoutService.removeWorkoutByDate(workoutDto.getDate()));
    }

    @Test
    void updateWorkoutShouldThrowIfWorkoutDoesNotExist(){
        Workout workout = Instancio.create(Workout.class);

        WorkoutService workoutService = new WorkoutService(workoutRepository);

        Assertions.assertThrows(NotFoundException.class, () -> workoutService.updateWorkout(workout));
    }

    @Test
    void updateWorkoutShouldSucceedIfWorkoutExists(){
        WorkoutDto workoutDto = Instancio.create(WorkoutDto.class);
        when(workoutRepository.existsById(workoutDto.getDate())).thenReturn(true);

        WorkoutService workoutService = new WorkoutService(workoutRepository);

        Assertions.assertDoesNotThrow(() -> workoutService.updateWorkout(Workout.fromDto(workoutDto)));
    }
}
