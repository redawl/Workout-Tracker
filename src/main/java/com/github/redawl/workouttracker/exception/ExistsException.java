package com.github.redawl.workouttracker.exception;

public class ExistsException extends Exception {
    public ExistsException(String className){
        super(className + " already exists");
    }
}
