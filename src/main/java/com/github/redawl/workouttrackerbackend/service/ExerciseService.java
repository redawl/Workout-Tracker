package com.github.redawl.workouttrackerbackend.service;

import com.github.redawl.workouttrackerbackend.model.data.Exercise;
import com.github.redawl.workouttrackerbackend.model.repository.ExerciseRepository;
import org.springframework.stereotype.Service;

@Service
public class ExerciseService {
    private final ExerciseRepository exerciseRepository;

    ExerciseService(ExerciseRepository exerciseRepository){
        this.exerciseRepository = exerciseRepository;
    }

    public Exercise getExerciseByName(String name){
        return exerciseRepository.findExerciseByName(name);
    }

    public void addExercise(Exercise exercise){
        exerciseRepository.save(exercise);
    }
}
