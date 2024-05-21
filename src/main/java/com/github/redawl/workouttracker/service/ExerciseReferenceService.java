package com.github.redawl.workouttracker.service;

import com.github.redawl.workouttracker.exception.NotFoundException;
import com.github.redawl.workouttracker.model.data.Exercise;
import com.github.redawl.workouttracker.model.repository.ExerciseReferenceRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
     * @return Exercise if it exists
     * @throws NotFoundException If Exercise with that name does not exist
     */
    public Exercise getExerciseByName(String name) throws NotFoundException {
        return Exercise.fromReferenceDto(exerciseReferenceRepository.findByName(name)
                .orElseThrow(() -> new NotFoundException("exercise"))
        );
    }

    /**
     * Retrieve list of all managed exercises.
     * @return List of exercises
     */
    public List<Exercise> getExercises() {
        List<Exercise> exercises = new ArrayList<>();
        exerciseReferenceRepository
                .findAll()
                .forEach(exerciseReferenceDto -> exercises.add(Exercise.fromReferenceDto(exerciseReferenceDto)));
        return exercises;
    }
}
