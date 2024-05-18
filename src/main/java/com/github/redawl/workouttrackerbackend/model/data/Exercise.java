package com.github.redawl.workouttrackerbackend.model.data;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Exercise {
    @Id
    private String name;
    private Integer lbs;
    private Integer reps;
    private Integer sets;
}
