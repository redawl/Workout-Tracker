package com.github.redawl.workouttracker.model.repository;

import com.github.redawl.workouttracker.model.dto.UserDto;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<UserDto, String> {
}
