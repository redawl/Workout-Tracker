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

@Service
public class WorkoutService {
    private final WorkoutRepository workoutRepository;

    public WorkoutService(WorkoutRepository workoutRepository){
        this.workoutRepository = workoutRepository;
    }

    public List<Workout> getWorkouts() {
        List<Workout> workouts = new ArrayList<>();
        workoutRepository.findAll().forEach(workoutDto ->  workouts.add(Workout.fromDto(workoutDto)));

        return workouts;
    }

    public void addWorkout(Workout workout) throws ExistsException {
        if(workoutRepository.existsById(workout.getDate())){
            throw new ExistsException("workout");
        }

        workoutRepository.save(WorkoutDto.from(workout));
    }

    public Workout getWorkoutByDate(LocalDate date) throws NotFoundException {
        return Workout.fromDto(
                workoutRepository.findById(date).orElseThrow(() -> new NotFoundException("workout"))
        );
    }

    public void removeWorkoutByDate(LocalDate date) throws NotFoundException {
        workoutRepository.delete(workoutRepository.findById(date).orElseThrow(() -> new NotFoundException("workout")));
    }

    public void updateWorkout(Workout workout) throws NotFoundException {
        if(!workoutRepository.existsById(workout.getDate())){
            throw new NotFoundException("workout");
        }

        workoutRepository.save(WorkoutDto.from(workout));
    }
}
