package com.cfuv.olympus.domain.exception.entity.contest;

public class ContestNotStartedException extends RuntimeException{
    public ContestNotStartedException(final String message){
        super(message);
    }
}
