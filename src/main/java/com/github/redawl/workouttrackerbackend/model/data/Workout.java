package com.github.redawl.workouttrackerbackend.model.data;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

@Entity
@Data
public class Workout {
    @Id
    private BigDecimal id;
    private LocalDate date;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "WORKOUT_EXERCISE",
            joinColumns = {
                    @JoinColumn(name = "EXERCISE_NAME")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "WORKOUT_ID")
            }
    )
    private Set<Exercise> exercises;
}
