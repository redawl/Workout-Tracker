package com.github.redawl.workouttracker.model.repository;

import com.github.redawl.workouttracker.model.dto.ExerciseReferenceDto;
import org.springframework.data.repository.CrudRepository;

import java.math.BigDecimal;

public interface ExerciseReferenceRepository extends CrudRepository<ExerciseReferenceDto, BigDecimal> {
    ExerciseReferenceDto findByName(String name);
}
