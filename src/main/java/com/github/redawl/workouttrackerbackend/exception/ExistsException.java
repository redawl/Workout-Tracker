package com.github.redawl.workouttrackerbackend.exception;

public class ExistsException extends Exception {
    public ExistsException(String className){
        super(className + " already exists");
    }
}
