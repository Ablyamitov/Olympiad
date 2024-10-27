package com.cfuv.olympus.domain.exception.entity.user;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(final String message) {
        super(message);
    }
}
