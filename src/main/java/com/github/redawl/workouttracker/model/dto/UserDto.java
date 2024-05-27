package com.github.redawl.workouttracker.model.dto;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Set;

@Entity(name = "WT_USER")
@Data
public class UserDto {
    @Id
    @NotNull
    @Size(
            min = 36,
            max = 36
    )
    private String id;

    @OneToMany
    @JoinColumn(name = "USER_ID")
    private Set<WorkoutDto> workouts;

    public static UserDto from(String id){
        UserDto dto = new UserDto();
        dto.setId(id);

        return dto;
    }
}
