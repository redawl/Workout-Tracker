package com.github.redawl.workouttrackerbackend.service;

import com.github.redawl.workouttrackerbackend.exception.ExistsException;
import com.github.redawl.workouttrackerbackend.exception.NotFoundException;
import com.github.redawl.workouttrackerbackend.model.data.Exercise;
import com.github.redawl.workouttrackerbackend.model.dto.ExerciseDto;
import com.github.redawl.workouttrackerbackend.model.repository.ExerciseRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ExerciseService {
    private final ExerciseRepository exerciseRepository;

    ExerciseService(ExerciseRepository exerciseRepository){
        this.exerciseRepository = exerciseRepository;
    }

    public void addExercise(Exercise exercise) throws ExistsException {

        exerciseRepository.save(ExerciseDto.from(exercise));
    }

    public void removeExerciseByName(String name) throws NotFoundException {
        ExerciseDto exerciseDto = exerciseRepository.findExerciseByName(name);

        if(exerciseDto != null){
            exerciseRepository.delete(exerciseDto);
        } else {
            throw new NotFoundException("exercise");
        }
    }

    public List<Exercise> getExercises() {
        List<Exercise> exercises = new ArrayList<>();
        exerciseRepository
                .findAll()
                .forEach(exerciseDto -> exercises.add(Exercise.fromDto(exerciseDto)));

        return exercises;
    }
}
