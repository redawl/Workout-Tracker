package com.github.redawl.workouttracker.service;

import com.github.redawl.workouttracker.exception.ExistsException;
import com.github.redawl.workouttracker.exception.NotFoundException;
import com.github.redawl.workouttracker.model.data.Workout;
import com.github.redawl.workouttracker.model.dto.ExerciseReferenceDto;
import com.github.redawl.workouttracker.model.dto.UserDto;
import com.github.redawl.workouttracker.model.dto.WorkoutDto;
import com.github.redawl.workouttracker.model.repository.ExerciseReferenceRepository;
import com.github.redawl.workouttracker.model.repository.UserRepository;
import com.github.redawl.workouttracker.model.repository.WorkoutRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Manage workouts for each day
 *
 * @author Eli Burch
 */
@Service
public class WorkoutService {
    private final WorkoutRepository workoutRepository;
    private final UserRepository userRepository;
    private final ExerciseReferenceRepository exerciseReferenceRepository;

    public WorkoutService(WorkoutRepository workoutRepository, UserRepository userRepository,
                          ExerciseReferenceRepository exerciseReferenceRepository){
        this.workoutRepository = workoutRepository;
        this.userRepository = userRepository;
        this.exerciseReferenceRepository = exerciseReferenceRepository;
    }

    /**
     * Retrieve a workout by its workout date
     * @param date Date of workout to retrieve
     * @param userJwt token of current user
     * @return Workout if it exists
     * @throws NotFoundException If workout does not exist
     */
    public Workout getWorkoutByDate(LocalDate date, String userJwt) throws NotFoundException {
        return Workout.fromDto(
                workoutRepository.findByDateAndUser(date, UserDto.from(userJwt))
                        .orElseThrow(() -> new NotFoundException(this.getClass().getName()))
        );
    }

    /**
     * Retrieve all currently saved workouts
     * @param userJwt token of current user
     * @return List of workouts
     */
    public List<Workout> getWorkouts(String userJwt) {
        List<Workout> workouts = new ArrayList<>();
        workoutRepository.findAllByUser(UserDto.from(userJwt)).forEach(workoutDto ->  workouts.add(Workout.fromDto(workoutDto)));

        return workouts;
    }

    /**
     * Add a workout to application
     * @param workout Workout to add
     * @param userJwt token of current user
     * @throws ExistsException If a workout with same workout date already exists
     */
    public void addWorkout(Workout workout, String userJwt) throws ExistsException {

        if(workoutRepository.existsByDateAndUser(workout.getDate(), UserDto.from(userJwt))){
            throw new ExistsException(this.getClass().getName());
        }

        List<ExerciseReferenceDto> exerciseReferences = exerciseReferenceRepository.findAllByUser(UserDto.from(userJwt));

        WorkoutDto dto = WorkoutDto.from(workout, userJwt, exerciseReferences);

        userRepository.findById(userJwt).ifPresent(dto::setUser);

        workoutRepository.save(dto);
    }

    /**
     * Remove a workout by its workout date
     * @param date Date of workout to delete
     * @param userJwt token of current user
     * @throws NotFoundException If workout with that date does not exist
     */
    public void removeWorkoutByDate(LocalDate date, String userJwt) throws NotFoundException {
        workoutRepository.delete(workoutRepository
                .findByDateAndUser(date, UserDto.from(userJwt))
                .orElseThrow(() -> new NotFoundException(this.getClass().getName())));
    }

    /**
     * Update an existing workout
     * @param workout Workout with updated values
     * @param userJwt token for current user
     * @throws NotFoundException If workout with same date does not exist
     */
    public void updateWorkout(Workout workout, String userJwt) throws NotFoundException {
        Optional<WorkoutDto> dto = workoutRepository.findByDateAndUser(workout.getDate(), UserDto.from(userJwt));

        List<ExerciseReferenceDto> exerciseReferences = exerciseReferenceRepository.findAllByUser(UserDto.from(userJwt));

        if(dto.isPresent()){
            WorkoutDto newDto = WorkoutDto.from(workout, userJwt, exerciseReferences);
            newDto.setId(dto.get().getId());

            workoutRepository.save(newDto);
        } else {
            throw new NotFoundException(this.getClass().getName());
        }
    }
}
