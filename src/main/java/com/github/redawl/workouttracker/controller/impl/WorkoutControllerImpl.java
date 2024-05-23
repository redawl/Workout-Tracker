package com.github.redawl.workouttracker.controller.impl;

import com.github.redawl.workouttracker.controller.WorkoutController;
import com.github.redawl.workouttracker.exception.ExistsException;
import com.github.redawl.workouttracker.exception.NotFoundException;
import com.github.redawl.workouttracker.model.data.Workout;
import com.github.redawl.workouttracker.service.WorkoutService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/workout")
@CrossOrigin(origins = {
        "http://127.0.0.1",
        "http://localhost",
        "http://localhost:5173"
})
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
            return workoutService.getWorkoutByDate(date);
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
            workoutService.removeWorkoutByDate(date);
        } catch (NotFoundException ex){
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    @PostMapping
    public void addWorkout(@RequestBody Workout workout, HttpServletResponse response) {
        try{
            response.setStatus(HttpServletResponse.SC_CREATED);
            workoutService.addWorkout(workout);
        } catch (ExistsException ex) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    @PutMapping
    public void updateWorkout(@RequestBody Workout workout, HttpServletResponse response) {
        try {
            response.setStatus(HttpServletResponse.SC_OK);
            workoutService.updateWorkout(workout);
        } catch (NotFoundException ex){
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    @GetMapping("/all")
    public List<Workout> getAllWorkouts() {
        return workoutService.getWorkouts();
    }
}
