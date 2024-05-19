package com.github.redawl.workouttrackerbackend.model.repository;

import com.github.redawl.workouttrackerbackend.model.dto.WorkoutDto;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;

public interface WorkoutRepository extends CrudRepository<WorkoutDto, LocalDate> {
}
