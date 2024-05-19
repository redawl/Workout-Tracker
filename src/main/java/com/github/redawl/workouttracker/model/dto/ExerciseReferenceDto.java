package com.github.redawl.workouttracker.model.dto;

import com.github.redawl.workouttracker.model.data.Exercise;
import jakarta.persistence.*;
import org.hibernate.annotations.CascadeType;
import lombok.Data;
import org.hibernate.annotations.Cascade;

@Entity
@Data
@Table(name = "EXERCISE_REFERENCE")
public class ExerciseReferenceDto {
    @Id
    private String name;

    @OneToOne
    @JoinColumn(name = "EXERCISE_ID")
    @Cascade(value = CascadeType.ALL)
    private ExerciseDto exercise;

    public static ExerciseReferenceDto fromExercise(Exercise exercise){
        ExerciseReferenceDto dto = new ExerciseReferenceDto();
        dto.setName(exercise.getName());
        dto.setExercise(
                ExerciseDto.from(exercise)
        );

        return dto;
    }
}
