package com.github.redawl.workouttrackerbackend.model.dto;

import com.github.redawl.workouttrackerbackend.model.data.Exercise;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.math.BigDecimal;

@Entity(name = "EXERCISE")
@Data
public class ExerciseDto {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private BigDecimal id;
    private String name;
    private Integer lbs;
    private Integer reps;
    private Integer sets;

    public static ExerciseDto from(Exercise exercise){
        ExerciseDto dto = new ExerciseDto();
        dto.setName(exercise.getName());
        dto.setLbs(exercise.getLbs());
        dto.setReps(exercise.getReps());
        dto.setSets(exercise.getSets());

        return dto;
    }
}
