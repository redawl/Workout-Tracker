package com.github.redawl.workouttracker.model.repository;

import com.github.redawl.workouttracker.model.dto.ExerciseDto;
import org.springframework.data.repository.CrudRepository;

import java.math.BigDecimal;

public interface ExerciseRepository extends CrudRepository<ExerciseDto, BigDecimal> {
    ExerciseDto findExerciseByName(String name);
}
