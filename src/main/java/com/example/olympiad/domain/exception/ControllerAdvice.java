package com.example.olympiad.domain.exception;

import com.example.olympiad.domain.exception.access.AccessDeniedException;
import com.example.olympiad.domain.exception.entity.ContestNotFoundException;
import com.example.olympiad.domain.exception.entity.ContestNotStartedException;
import com.example.olympiad.domain.exception.entity.UserNotFoundException;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.api.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;


@RestControllerAdvice
@Hidden
public class ControllerAdvice {
    public static final Logger log = LoggerFactory.getLogger(ControllerAdvice.class);

    @ExceptionHandler(ContestNotStartedException.class)
    public ResponseEntity<ErrorMessage> contestNotStartedException(ContestNotStartedException exception) {
        log.error(exception.getMessage(), exception);
        return ResponseEntity
                .status(HttpStatus.GATEWAY_TIMEOUT)
                .body(new ErrorMessage(exception.getMessage()));
    }


    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorMessage> entityNotFoundException(EntityNotFoundException exception) {
        log.error(exception.getMessage(), exception);
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorMessage(exception.getMessage()));
    }


    @ExceptionHandler(ContestNotFoundException.class)
    public ResponseEntity<ErrorMessage> contestNotFoundException(ContestNotFoundException exception) {
        log.error(exception.getMessage(), exception);
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorMessage(exception.getMessage()));
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorMessage> userNotFoundException(UserNotFoundException exception) {
        log.error(exception.getMessage(), exception);
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorMessage(exception.getMessage()));
    }


    @ExceptionHandler({AccessDeniedException.class, org.springframework.security.access.AccessDeniedException.class})
    public ResponseEntity<ErrorMessage> accessDeniedException() {
        log.error("Access denied");
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(new ErrorMessage("Access denied"));
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<ErrorMessage> ioException(IOException exception) {
        log.error(exception.getMessage(), exception);
        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(new ErrorMessage(exception.getMessage()));
    }


}
