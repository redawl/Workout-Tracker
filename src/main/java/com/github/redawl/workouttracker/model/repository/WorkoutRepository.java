package com.github.redawl.workouttracker.model.repository;

import com.github.redawl.workouttracker.model.dto.UserDto;
import com.github.redawl.workouttracker.model.dto.WorkoutDto;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface WorkoutRepository extends CrudRepository<WorkoutDto, LocalDate> {
    Optional<WorkoutDto> findByDateAndUser(LocalDate date, UserDto user);
    Iterable<WorkoutDto> findAllByUser(UserDto user);
    boolean existsByDateAndUser(LocalDate date, UserDto user);
}
