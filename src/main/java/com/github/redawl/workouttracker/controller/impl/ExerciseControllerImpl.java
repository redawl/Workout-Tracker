package com.github.redawl.workouttracker.controller.impl;

import com.github.redawl.workouttracker.controller.ExerciseController;
import com.github.redawl.workouttracker.exception.NotFoundException;
import com.github.redawl.workouttracker.exception.UnauthorizedException;
import com.github.redawl.workouttracker.model.data.Exercise;
import com.github.redawl.workouttracker.service.ExerciseReferenceService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/exercise")
@CrossOrigin(origins = {
        "https://workout.burchbytes.com",
        "http://127.0.0.1",
        "http://localhost",
        "http://192.168.50.180"
})
@SuppressWarnings("unused")
public class ExerciseControllerImpl implements ExerciseController {
    private final ExerciseReferenceService exerciseReferenceService;

    ExerciseControllerImpl(ExerciseReferenceService exerciseReferenceService){
        this.exerciseReferenceService = exerciseReferenceService;
    }

    @Override
    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<Exercise> getExercises() {
        return exerciseReferenceService.getExercises(getUserJwt());
    }

    @Override
    @GetMapping
    public Exercise getExerciseByName(String name, HttpServletResponse response) {
        try{
            response.setStatus(HttpServletResponse.SC_OK);
            return exerciseReferenceService.getExerciseByName(name, getUserJwt());
        } catch (NotFoundException ex){
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }

        return null;
    }

    private String getUserJwt(){
        Object credentials = SecurityContextHolder.getContext().getAuthentication() == null ? null
                : SecurityContextHolder.getContext().getAuthentication().getCredentials();

        if(credentials instanceof String userJwt){
            return userJwt;
        }

        throw new UnauthorizedException();
    }
}
