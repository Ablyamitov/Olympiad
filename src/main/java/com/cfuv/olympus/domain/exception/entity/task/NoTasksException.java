package com.cfuv.olympus.domain.exception.entity.task;

public class NoTasksException extends RuntimeException{
    public NoTasksException(final String message){
        super(message);
    }
}
