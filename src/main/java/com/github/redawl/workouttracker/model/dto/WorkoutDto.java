package com.github.redawl.workouttracker.model.dto;

import com.github.redawl.workouttracker.model.data.Workout;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

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
    @Column(name = "WORKOUT_DATE")
    @NotNull
    private LocalDate date;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "WORKOUT_ID")
    @Cascade(value = CascadeType.ALL)
    @NotNull
    private List<ExerciseDto> exerciseDtos;

    /**
     * Generates a WorkoutDto from a workout
     * @param workout workout to transform
     * @return WorkoutDto representing the passed in workout
     */
    public static WorkoutDto from(Workout workout){
        WorkoutDto dto = new WorkoutDto();
        dto.setDate(workout.getDate());

        dto.setExerciseDtos(workout.getExercises()
                .stream()
                .map((ExerciseDto::from))
                .toList()
        );

        return dto;
    }
}
