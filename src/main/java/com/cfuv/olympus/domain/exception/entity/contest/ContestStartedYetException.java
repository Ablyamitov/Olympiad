package com.cfuv.olympus.domain.exception.entity.contest;

public class ContestStartedYetException extends RuntimeException{
    public ContestStartedYetException(final String message){
        super(message);
    }
}
