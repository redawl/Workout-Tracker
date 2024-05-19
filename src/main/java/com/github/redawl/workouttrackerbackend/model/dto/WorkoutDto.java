package com.github.redawl.workouttrackerbackend.model.dto;

import com.github.redawl.workouttrackerbackend.model.data.Exercise;
import com.github.redawl.workouttrackerbackend.model.data.Workout;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity(name = "WORKOUT")
@Data
public class WorkoutDto {
    @Id
    @Column(name = "WORKOUT_DATE")
    private LocalDate date;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "WORKOUT_ID")
    @Cascade(value = CascadeType.ALL)
    private List<ExerciseDto> exerciseDtos;

    public static WorkoutDto from (Workout workout){
        WorkoutDto dto = new WorkoutDto();
        dto.setDate(workout.getDate());
        dto.setExerciseDtos(new ArrayList<>());

        for(Exercise exercise: workout.getExercises()){
            dto.getExerciseDtos().add(ExerciseDto.from(exercise));
        }

        return dto;
    }
}
