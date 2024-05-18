package com.github.redawl.workouttrackerbackend.model.repository;

import com.github.redawl.workouttrackerbackend.model.data.Exercise;
import org.springframework.data.repository.CrudRepository;

public interface ExerciseRepository extends CrudRepository<Exercise, String> {
    Exercise findExerciseByName(String name);
}
