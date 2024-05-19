package com.github.redawl.workouttrackerbackend.model.repository;

import com.github.redawl.workouttrackerbackend.model.dto.ExerciseReferenceDto;
import org.springframework.data.repository.CrudRepository;

import java.math.BigDecimal;

public interface ExerciseReferenceRepository extends CrudRepository<ExerciseReferenceDto, BigDecimal> {
    ExerciseReferenceDto findByName(String name);
}
