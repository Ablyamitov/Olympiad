package com.example.olympiad.domain.exception;

public class ContestNotStartedExeption extends RuntimeException{
    public ContestNotStartedExeption(final String message){
        super(message);
    }
}
