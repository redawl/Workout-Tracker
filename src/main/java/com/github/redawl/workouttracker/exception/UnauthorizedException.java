package com.github.redawl.workouttracker.exception;

public class UnauthorizedException extends RuntimeException{
    public UnauthorizedException(){
        super("Unauthorized user got to endpoint that requires authorization!");
    }
}
