package com.github.redawl.workouttracker.model.dto;

import com.github.redawl.workouttracker.model.data.Workout;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.List;

/**
 * Represents a workout for a given day.
 *
 * @author Eli Burch
 */
@Entity(name = "WORKOUT")
@Data
public class WorkoutDto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private BigInteger id;

    @Column(name = "WORKOUT_DATE")
    @NotNull
    private LocalDate date;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "workout", orphanRemoval = true)
    @Cascade(value = CascadeType.ALL)
    @NotNull
    private List<ExerciseDto> exercises;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    @Cascade(value = CascadeType.PERSIST)
    private UserDto user;

    /**
     * Generates a WorkoutDto from a workout<br>
     * @param workout workout to transform
     * @param userJwt token of current user
     * @param exerciseReferences existing references available for exercises
     * @return WorkoutDto representing the passed in workout
     */
    public static WorkoutDto from(Workout workout, String userJwt, List<ExerciseReferenceDto> exerciseReferences) {
        WorkoutDto dto = new WorkoutDto();
        dto.setDate(workout.getDate());

        dto.setExercises(workout.getExercises().stream()
                .map(exercise -> {
                    ExerciseDto exerciseDto = ExerciseDto.from(exercise, userJwt);
                    exerciseDto.setWorkout(dto);
                    if(exerciseReferences.stream()
                            .noneMatch(exerciseReference -> exerciseReference.getName().equals(exercise.getName()))){
                        exerciseDto.setExerciseReference(ExerciseReferenceDto.fromExercise(exercise, userJwt));
                        exerciseDto.getExerciseReference().setExercises(List.of(exerciseDto));
                    } else {
                        ExerciseReferenceDto exerciseReferenceDto = exerciseReferences.stream()
                                .filter(exerciseReference -> exerciseReference.getName().equals(exercise.getName()))
                                .findAny()
                                .orElseThrow();
                        exerciseReferenceDto.getExercises().add(exerciseDto);
                        exerciseDto.setExerciseReference(exerciseReferenceDto);
                    }
                    return exerciseDto;
                })
                .toList());

        dto.setUser(UserDto.from(userJwt));

        return dto;
    }
}
