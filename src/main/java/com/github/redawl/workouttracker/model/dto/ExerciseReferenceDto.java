package com.github.redawl.workouttracker.model.dto;

import com.github.redawl.workouttracker.model.data.Exercise;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.CascadeType;
import lombok.Data;
import org.hibernate.annotations.Cascade;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@EqualsAndHashCode(exclude = "exercises")
@ToString(exclude = "exercises")
@Table(name = "EXERCISE_REFERENCE")
public class ExerciseReferenceDto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private BigInteger id;

    @NotNull
    private String name;

    @OneToMany(mappedBy = "exerciseReference")
    @Cascade(value = CascadeType.ALL)
    private List<ExerciseDto> exercises;

    @ManyToOne
    @JoinColumn(name="USER_ID")
    private UserDto user;

    public void setExercises(List<ExerciseDto> exercises){
        for(ExerciseDto exercise: exercises){
            exercise.setExerciseReference(this);
        }
        this.exercises = exercises;
    }

    public static ExerciseReferenceDto fromExercise(Exercise exercise, String userJwt){
        ExerciseReferenceDto dto = new ExerciseReferenceDto();
        dto.setName(exercise.getName());
        dto.setExercises(new ArrayList<>());
        dto.getExercises().add(ExerciseDto.from(exercise, userJwt));
        dto.setUser(UserDto.from(userJwt));

        return dto;
    }

}
