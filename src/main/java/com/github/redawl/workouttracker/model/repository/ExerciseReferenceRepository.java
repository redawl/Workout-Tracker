package com.github.redawl.workouttracker.model.repository;

import com.github.redawl.workouttracker.model.dto.ExerciseReferenceDto;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.math.BigDecimal;
import java.util.List;

public interface ExerciseReferenceRepository extends CrudRepository<ExerciseReferenceDto, BigDecimal> {
    ExerciseReferenceDto findByName(String name);

    @Query(
            value = "SELECT * FROM EXERCISE_REFERENCE WHERE to_tsvector('english', name) @@ to_tsquery('english', :name)",
            nativeQuery = true
    )
    List<ExerciseReferenceDto> searchByName(String name);
}
