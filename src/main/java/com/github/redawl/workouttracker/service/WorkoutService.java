package com.github.redawl.workouttracker.service;

import com.github.redawl.workouttracker.exception.ExistsException;
import com.github.redawl.workouttracker.exception.NotFoundException;
import com.github.redawl.workouttracker.model.data.Workout;
import com.github.redawl.workouttracker.model.dto.WorkoutDto;
import com.github.redawl.workouttracker.model.repository.WorkoutRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Manage workouts for each day
 *
 * @author Eli Burch
 */
@Service
public class WorkoutService {
    private final WorkoutRepository workoutRepository;

    public WorkoutService(WorkoutRepository workoutRepository){
        this.workoutRepository = workoutRepository;
    }

    /**
     * Retrieve a workout by its workout date
     * @param date Date of workout to retrieve
     * @return Workout if it exists
     * @throws NotFoundException If workout does not exist
     */
    public Workout getWorkoutByDate(LocalDate date) throws NotFoundException {
        return Workout.fromDto(
                workoutRepository.findById(date).orElseThrow(() -> new NotFoundException(this.getClass().getName()))
        );
    }

    /**
     * Retrieve all currently saved workouts
     * @return List of workouts
     */
    public List<Workout> getWorkouts() {
        List<Workout> workouts = new ArrayList<>();
        workoutRepository.findAll().forEach(workoutDto ->  workouts.add(Workout.fromDto(workoutDto)));

        return workouts;
    }

    /**
     * Add a workout to application
     * @param workout Workout to add
     * @throws ExistsException If a workout with same workout date already exists
     */
    public void addWorkout(Workout workout) throws ExistsException {
        if(workoutRepository.existsById(workout.getDate())){
            throw new ExistsException(this.getClass().getName());
        }

        workoutRepository.save(WorkoutDto.from(workout));
    }

    /**
     * Remove a workout by its workout date
     * @param date Date of workout to delete
     * @throws NotFoundException If workout with that date does not exist
     */
    public void removeWorkoutByDate(LocalDate date) throws NotFoundException {
        workoutRepository.delete(workoutRepository
                .findById(date)
                .orElseThrow(() -> new NotFoundException(this.getClass().getName())));
    }

    /**
     * Update an existing workout
     * @param workout Workout with updated values
     * @throws NotFoundException If workout with same date does not exist
     */
    public void updateWorkout(Workout workout) throws NotFoundException {
        if(!workoutRepository.existsById(workout.getDate())){
            throw new NotFoundException(this.getClass().getName());
        }

        workoutRepository.save(WorkoutDto.from(workout));
    }
}
