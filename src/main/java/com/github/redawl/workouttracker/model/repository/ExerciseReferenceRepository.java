package com.github.redawl.workouttracker.model.repository;

import com.github.redawl.workouttracker.model.dto.ExerciseReferenceDto;
import org.springframework.data.repository.CrudRepository;

import java.math.BigDecimal;
import java.util.Optional;

public interface ExerciseReferenceRepository extends CrudRepository<ExerciseReferenceDto, BigDecimal> {
    Optional<ExerciseReferenceDto> findByName(String name);
}
