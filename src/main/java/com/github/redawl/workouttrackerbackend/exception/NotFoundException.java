package com.github.redawl.workouttrackerbackend.exception;

public class NotFoundException extends Exception {
    public NotFoundException(String className){
        super(className + " not found");
    }
}
