package com.github.redawl.workouttracker.model.dto;

import com.github.redawl.workouttracker.model.data.Exercise;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.math.BigInteger;

@Entity(name = "EXERCISE")
@Data
@EqualsAndHashCode(exclude = "workout")
@ToString(exclude = "workout")
public class ExerciseDto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private BigInteger id;

    @NotNull
    private Integer lbs;

    @NotNull
    private Integer reps;

    @NotNull
    private Integer sets;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    @NotNull
    private UserDto user;

    @ManyToOne
    @JoinColumn(name = "WORKOUT_ID", nullable = false, updatable = false)
    @NotNull
    private WorkoutDto workout;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "EXERCISE_REFERENCE_ID", nullable = false)
    @NotNull
    private ExerciseReferenceDto exerciseReference;


    /**
     * Create new ExerciseDto from an exercise and user token
     * @param exercise Exercise to create from
     * @param userJwt token of current user
     * @return Newly created ExerciseDto
     */
    public static ExerciseDto from(Exercise exercise, String userJwt){
        ExerciseDto dto = new ExerciseDto();
        dto.setLbs(exercise.getLbs());
        dto.setReps(exercise.getReps());
        dto.setSets(exercise.getSets());
        dto.setUser(UserDto.from(userJwt));

        return dto;
    }
}
