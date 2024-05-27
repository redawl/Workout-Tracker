package com.github.redawl.workouttracker.controller.impl;

import com.github.redawl.workouttracker.controller.WorkoutController;
import com.github.redawl.workouttracker.exception.ExistsException;
import com.github.redawl.workouttracker.exception.NotFoundException;
import com.github.redawl.workouttracker.model.data.Workout;
import com.github.redawl.workouttracker.service.WorkoutService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/workout")
@CrossOrigin(origins = {
        "https://workout.burchbytes.com",
        "http://127.0.0.1",
        "http://localhost",
        "http://192.168.50.180"
})
@SuppressWarnings("unused")
public class WorkoutControllerImpl implements WorkoutController {
    private final WorkoutService workoutService;

    public WorkoutControllerImpl(WorkoutService workoutService){
        this.workoutService = workoutService;
    }

    @Override
    @GetMapping
    public Workout getWorkoutByDate(@RequestParam LocalDate date, HttpServletResponse response) {
        try{
            response.setStatus(HttpServletResponse.SC_OK);
            return workoutService.getWorkoutByDate(date, getUserJwt());
        } catch (NotFoundException ex) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return null;
        }
    }

    @Override
    @DeleteMapping
    public void removeWorkoutByDate(@RequestParam LocalDate date, HttpServletResponse response) {
        try{
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
            workoutService.removeWorkoutByDate(date, getUserJwt());
        } catch (NotFoundException ex){
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    @PostMapping
    public void addWorkout(@RequestBody Workout workout, HttpServletResponse response) {
        try{
            response.setStatus(HttpServletResponse.SC_CREATED);
            workoutService.addWorkout(workout, getUserJwt());
        } catch (ExistsException ex) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    @PutMapping
    public void updateWorkout(@RequestBody Workout workout, HttpServletResponse response) {
        try {
            response.setStatus(HttpServletResponse.SC_OK);
            workoutService.updateWorkout(workout, getUserJwt());
        } catch (NotFoundException ex){
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    @GetMapping("/all")
    public List<Workout> getAllWorkouts() {
        return workoutService.getWorkouts(getUserJwt());
    }

    private String getUserJwt(){
        Object credentials = SecurityContextHolder.getContext().getAuthentication() == null ? null
                : SecurityContextHolder.getContext().getAuthentication().getCredentials();

        if(credentials instanceof String userJwt){
            return userJwt;
        }

        throw new RuntimeException("Unauthorized user got to endpoint that requires authorization!");
    }
}
