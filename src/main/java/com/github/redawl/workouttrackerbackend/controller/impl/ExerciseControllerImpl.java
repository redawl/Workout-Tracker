package com.github.redawl.workouttrackerbackend.controller.impl;

import com.github.redawl.workouttrackerbackend.controller.ExerciseController;
import com.github.redawl.workouttrackerbackend.exception.ExistsException;
import com.github.redawl.workouttrackerbackend.exception.NotFoundException;
import com.github.redawl.workouttrackerbackend.model.data.Exercise;
import com.github.redawl.workouttrackerbackend.model.dto.ExerciseDto;
import com.github.redawl.workouttrackerbackend.service.ExerciseService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/exercise")
public class ExerciseControllerImpl implements ExerciseController {
    private final ExerciseService exerciseService;

    ExerciseControllerImpl(ExerciseService exerciseService){
        this.exerciseService = exerciseService;
    }

    @Override
    @DeleteMapping()
    public void removeExercise(@RequestParam String name, HttpServletResponse response) {
        try {
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
            exerciseService.removeExerciseByName(name);
        } catch (NotFoundException ex) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<Exercise> getExercises() {
        return exerciseService.getExercises();
    }

    @Override
    @PostMapping
    public void addExercise(@RequestBody Exercise exercise, HttpServletResponse response) {
        try {
            response.setStatus(HttpServletResponse.SC_CREATED);
            exerciseService.addExercise(exercise);
        } catch (ExistsException ex){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}
