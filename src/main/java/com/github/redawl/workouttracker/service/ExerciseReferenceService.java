package com.github.redawl.workouttracker.service;

import com.github.redawl.workouttracker.exception.NotFoundException;
import com.github.redawl.workouttracker.model.data.Exercise;
import com.github.redawl.workouttracker.model.dto.UserDto;
import com.github.redawl.workouttracker.model.repository.ExerciseReferenceRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Retrieve information about the currently available exercises.
 *
 * @author Eli Burch
 */
@Service
public class ExerciseReferenceService {
    private final ExerciseReferenceRepository exerciseReferenceRepository;

    ExerciseReferenceService(ExerciseReferenceRepository exerciseReferenceRepository){
        this.exerciseReferenceRepository = exerciseReferenceRepository;
    }

    /**
     * Retrieve Exercise by name.
     * @param name Name of exercise to retrieve
     * @param userJwt token of current user
     * @return Exercise if it exists
     * @throws NotFoundException If Exercise with that name does not exist
     */
    public Exercise getExerciseByName(String name, String userJwt) throws NotFoundException {
        return Exercise.fromReferenceDto(exerciseReferenceRepository.findByNameAndUser(name, UserDto.from(userJwt))
                .orElseThrow(() -> new NotFoundException("exercise"))
        );
    }

    /**
     * Retrieve list of all managed exercises.
     * @param userJwt token of current user
     * @return List of exercises
     */
    public List<Exercise> getExercises(String userJwt) {
        return exerciseReferenceRepository
                .findAllByUser(UserDto.from(userJwt)).stream()
                .map(Exercise::fromReferenceDto)
                .toList();
    }
}
