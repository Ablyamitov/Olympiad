package com.cfuv.olympus.domain.exception.entity.contest;

public class ContestNotFoundException extends RuntimeException {
    public ContestNotFoundException(final String message) {
        super(message);
    }
}
