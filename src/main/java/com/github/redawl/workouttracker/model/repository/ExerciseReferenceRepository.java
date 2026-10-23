package com.github.redawl.workouttracker.model.repository;

import com.github.redawl.workouttracker.model.dto.ExerciseReferenceDto;
import com.github.redawl.workouttracker.model.dto.UserDto;
import org.springframework.data.repository.CrudRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ExerciseReferenceRepository extends CrudRepository<ExerciseReferenceDto, BigDecimal> {
    Optional<ExerciseReferenceDto> findByNameAndUser(String name, UserDto user);
    List<ExerciseReferenceDto> findAllByUser(UserDto user);
}
