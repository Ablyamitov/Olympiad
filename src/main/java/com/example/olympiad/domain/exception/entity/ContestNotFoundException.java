package com.example.olympiad.domain.exception.entity;

public class ContestNotFoundException extends RuntimeException {
    public ContestNotFoundException(final String message) {
        super(message);
    }
}
