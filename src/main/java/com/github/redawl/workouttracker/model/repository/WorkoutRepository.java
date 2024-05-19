package com.github.redawl.workouttracker.model.repository;

import com.github.redawl.workouttracker.model.dto.WorkoutDto;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;

public interface WorkoutRepository extends CrudRepository<WorkoutDto, LocalDate> {
}
