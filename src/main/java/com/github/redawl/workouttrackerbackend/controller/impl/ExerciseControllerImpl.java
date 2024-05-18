package com.github.redawl.workouttrackerbackend.controller.impl;

import com.github.redawl.workouttrackerbackend.controller.ExerciseController;
import com.github.redawl.workouttrackerbackend.model.data.Exercise;
import com.github.redawl.workouttrackerbackend.service.ExerciseService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/exercise")
public class ExerciseControllerImpl implements ExerciseController {
    private ExerciseService exerciseService;

    ExerciseControllerImpl(ExerciseService exerciseService){
        this.exerciseService = exerciseService;
    }


    @Override
    @GetMapping()
    public Exercise getExercise(@RequestParam String name) {

        return exerciseService.getExerciseByName(name);
    }

    @Override
    @PostMapping
    public void addExercise(@RequestBody Exercise exercise) {
        exerciseService.addExercise(exercise);
    }
}
