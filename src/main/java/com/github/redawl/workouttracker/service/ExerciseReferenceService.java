package com.github.redawl.workouttracker.service;

import com.github.redawl.workouttracker.exception.ExistsException;
import com.github.redawl.workouttracker.exception.NotFoundException;
import com.github.redawl.workouttracker.model.data.Exercise;
import com.github.redawl.workouttracker.model.dto.ExerciseDto;
import com.github.redawl.workouttracker.model.dto.ExerciseReferenceDto;
import com.github.redawl.workouttracker.model.repository.ExerciseReferenceRepository;
import com.github.redawl.workouttracker.model.repository.ExerciseRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ExerciseReferenceService {
    private final ExerciseReferenceRepository exerciseReferenceRepository;

    private final ExerciseRepository exerciseRepository;

    ExerciseReferenceService(ExerciseReferenceRepository exerciseReferenceRepository, ExerciseRepository exerciseRepository){
        this.exerciseReferenceRepository = exerciseReferenceRepository;
        this.exerciseRepository = exerciseRepository;
    }

    public void addExercise(Exercise exercise) throws ExistsException {
        ExerciseReferenceDto exerciseReferenceDto = ExerciseReferenceDto.fromExercise(exercise);

        ExerciseDto existingExercise = exerciseRepository.findExerciseByName(exercise.getName());

        if(existingExercise != null){
            exerciseReferenceDto.getExercise().setId(existingExercise.getId());
        }

        exerciseReferenceRepository.save(exerciseReferenceDto);
    }

    public Exercise getExerciseByName(String name) throws NotFoundException {
        ExerciseReferenceDto exerciseReferenceDto = exerciseReferenceRepository.findByName(name);

        if(exerciseReferenceDto != null){
            return Exercise.fromReferenceDto(exerciseReferenceDto);
        } else {
            throw new NotFoundException("exercise");
        }
    }

    public List<Exercise> getExercises() {
        List<Exercise> exercises = new ArrayList<>();
        exerciseReferenceRepository
                .findAll()
                .forEach(exerciseReferenceDto -> exercises.add(Exercise.fromReferenceDto(exerciseReferenceDto)));
        return exercises;
    }
}
