package com.github.redawl.workouttracker.service;

import com.github.redawl.workouttracker.exception.ExistsException;
import com.github.redawl.workouttracker.exception.NotFoundException;
import com.github.redawl.workouttracker.model.data.Workout;
import com.github.redawl.workouttracker.model.dto.UserDto;
import com.github.redawl.workouttracker.model.dto.WorkoutDto;
import com.github.redawl.workouttracker.model.repository.ExerciseReferenceRepository;
import com.github.redawl.workouttracker.model.repository.UserRepository;
import com.github.redawl.workouttracker.model.repository.WorkoutRepository;
import org.instancio.Instancio;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class WorkoutServiceTests {
    @Mock
    WorkoutRepository workoutRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    ExerciseReferenceRepository exerciseReferenceRepository;

    @MockBean
    Authentication authentication;

    @BeforeAll
    void setupTests(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getWorkoutWithBadDateShouldThrow(){
        LocalDate date = Instancio.create(LocalDate.class);
        String userJwt = Instancio.create(String.class);

        WorkoutService workoutService = new WorkoutService(workoutRepository, userRepository, exerciseReferenceRepository);

        Assertions.assertThrows(NotFoundException.class, () -> workoutService.getWorkoutByDate(date, userJwt));
    }

    @Test
    void getWorkoutShouldReturnValid() throws NotFoundException {
        WorkoutDto workout = Instancio.create(WorkoutDto.class);
        String userJwt = Instancio.create(String.class);

        LocalDate date = workout.getDate();
        when(workoutRepository.findByDateAndUser(date, UserDto.from(userJwt))).thenReturn(Optional.of(workout));

        WorkoutService workoutService = new WorkoutService(workoutRepository, userRepository, exerciseReferenceRepository);

        Workout expected = Workout.fromDto(workout);
        Workout actual   = workoutService.getWorkoutByDate(date, userJwt);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void getWorkoutsShouldReturnExpected(){
        List<WorkoutDto> workoutDtos = Instancio.createList(WorkoutDto.class);
        String userJwt = Instancio.create(String.class);
        when(workoutRepository.findAllByUser(UserDto.from(userJwt))).thenReturn(workoutDtos);

        WorkoutService workoutService = new WorkoutService(workoutRepository, userRepository, exerciseReferenceRepository);

        List<Workout> expected = workoutDtos.stream()
                .map(Workout::fromDto)
                .toList();
        List<Workout> actual   = workoutService.getWorkouts(userJwt);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void addWorkoutShouldThrowIfDateAlreadyExists(){
        Workout workout = Instancio.create(Workout.class);
        String userJwt = Instancio.create(String.class);
        when(workoutRepository.existsByDateAndUser(workout.getDate(), UserDto.from(userJwt))).thenReturn(true);

        WorkoutService workoutService = new WorkoutService(workoutRepository, userRepository, exerciseReferenceRepository);

        Assertions.assertThrows(ExistsException.class, () -> workoutService.addWorkout(workout, userJwt));
    }

    @Test
    void addWorkoutShouldSucceedIfWorkoutDoesNotExist(){
        Workout workout = Instancio.create(Workout.class);
        String userJwt  = Instancio.create(String.class);
        when(workoutRepository.existsById(workout.getDate())).thenReturn(false);

        WorkoutService workoutService = new WorkoutService(workoutRepository, userRepository, exerciseReferenceRepository);

        Assertions.assertDoesNotThrow(() -> workoutService.addWorkout(workout, userJwt));
    }

    @Test
    void removeWorkoutShouldThrowIfWorkoutDoesNotExist(){
        LocalDate date = Instancio.create(LocalDate.class);
        String userJwt = Instancio.create(String.class);

        WorkoutService workoutService = new WorkoutService(workoutRepository, userRepository, exerciseReferenceRepository);

        Assertions.assertThrows(NotFoundException.class, () -> workoutService.removeWorkoutByDate(date, userJwt));
    }

    @Test
    void removeWorkoutShouldSucceedIfWorkoutExists(){
        WorkoutDto workoutDto = Instancio.create(WorkoutDto.class);
        String userJwt = Instancio.create(String.class);
        when(workoutRepository.findByDateAndUser(workoutDto.getDate(), UserDto.from(userJwt))).thenReturn(Optional.of(workoutDto));

        WorkoutService workoutService = new WorkoutService(workoutRepository, userRepository, exerciseReferenceRepository);

        Assertions.assertDoesNotThrow(() -> workoutService.removeWorkoutByDate(workoutDto.getDate(), userJwt));
    }

    @Test
    void updateWorkoutShouldThrowIfWorkoutDoesNotExist(){
        Workout workout = Instancio.create(Workout.class);
        String userJwt = Instancio.create(String.class);

        WorkoutService workoutService = new WorkoutService(workoutRepository, userRepository, exerciseReferenceRepository);

        Assertions.assertThrows(NotFoundException.class, () -> workoutService.updateWorkout(workout, userJwt));
    }

    @Test
    void updateWorkoutShouldSucceedIfWorkoutExists(){
        WorkoutDto workoutDto = Instancio.create(WorkoutDto.class);
        String userJwt = Instancio.create(String.class);
        when(workoutRepository.findByDateAndUser(workoutDto.getDate(), UserDto.from(userJwt)))
                .thenReturn(Optional.of(workoutDto));

        WorkoutService workoutService = new WorkoutService(workoutRepository, userRepository, exerciseReferenceRepository);

        Assertions.assertDoesNotThrow(() -> workoutService.updateWorkout(Workout.fromDto(workoutDto), userJwt));
    }
}
