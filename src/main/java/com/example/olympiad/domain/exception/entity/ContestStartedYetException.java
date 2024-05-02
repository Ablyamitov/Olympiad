package com.example.olympiad.domain.exception.entity;

public class ContestStartedYetException extends RuntimeException{
    public ContestStartedYetException(final String message){
        super(message);
    }
}
