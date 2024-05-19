package com.github.redawl.workouttracker.exception;

public class NotFoundException extends Exception {
    public NotFoundException(String className){
        super(className + " not found");
    }
}
