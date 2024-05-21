package com.github.redawl.workouttracker.controller.impl;

import com.github.redawl.workouttracker.controller.ExerciseController;
import com.github.redawl.workouttracker.exception.ExistsException;
import com.github.redawl.workouttracker.exception.NotFoundException;
import com.github.redawl.workouttracker.model.data.Exercise;
import com.github.redawl.workouttracker.service.ExerciseReferenceService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/exercise")
@CrossOrigin(origins = {
        "http://localhost:8080",
        "http://localhost:5173"
})
public class ExerciseControllerImpl implements ExerciseController {
    private final ExerciseReferenceService exerciseReferenceService;

    ExerciseControllerImpl(ExerciseReferenceService exerciseReferenceService){
        this.exerciseReferenceService = exerciseReferenceService;
    }

    @Override
    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<Exercise> getExercises() {
        return exerciseReferenceService.getExercises();
    }

    @Override
    @GetMapping("/search")
    public List<Exercise> searchExercisesByName(@RequestParam String name) {
        return exerciseReferenceService.searchByName(name);
    }

    @Override
    @PostMapping
    public void addExercise(@RequestBody Exercise exercise, HttpServletResponse response) {
        try {
            response.setStatus(HttpServletResponse.SC_CREATED);
            exerciseReferenceService.addExercise(exercise);
        } catch (ExistsException ex){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    @GetMapping
    public Exercise getExerciseByName(String name, HttpServletResponse response) {
        try{
            response.setStatus(HttpServletResponse.SC_OK);
            return exerciseReferenceService.getExerciseByName(name);
        } catch (NotFoundException ex){
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }

        return null;
    }
}
