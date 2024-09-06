package com.example.olympiad.domain.exception;

import com.example.olympiad.domain.exception.access.AccessDeniedException;
import com.example.olympiad.domain.exception.entity.ContestNotFoundException;
import com.example.olympiad.domain.exception.entity.ContestNotStartedException;
import com.example.olympiad.domain.exception.entity.ContestStartedYetException;
import com.example.olympiad.domain.exception.entity.UserNotFoundException;
import com.example.olympiad.web.dto.CustomResponse.CustomResponse;
import com.example.olympiad.web.dto.CustomResponse.ResponseUtil;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.api.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@RestControllerAdvice
public class ControllerAdvice {
    public static final Logger log = LoggerFactory.getLogger(ControllerAdvice.class);

    @ExceptionHandler(ContestNotStartedException.class)
    public ResponseEntity<ErrorMessage> handleContestNotStartedException(ContestNotStartedException exception) {
        log.error(exception.getMessage(), exception);
        return ResponseEntity
                .status(HttpStatus.GATEWAY_TIMEOUT)
                .body(new ErrorMessage(exception.getMessage()));
    }


    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorMessage> handleEntityNotFoundException(EntityNotFoundException exception) {
        log.error(exception.getMessage(), exception);
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorMessage(exception.getMessage()));
    }


    @ExceptionHandler(ContestNotFoundException.class)
    public ResponseEntity<ErrorMessage> handleContestNotFoundException(ContestNotFoundException exception) {
        log.error(exception.getMessage(), exception);
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorMessage(exception.getMessage()));
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorMessage> handleUserNotFoundException(UserNotFoundException exception) {
        log.error(exception.getMessage(), exception);
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorMessage(exception.getMessage()));
    }


    @ExceptionHandler({AccessDeniedException.class, org.springframework.security.access.AccessDeniedException.class})
    public ResponseEntity<ErrorMessage> handleAccessDeniedException() {
        log.error("Access denied");
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(new ErrorMessage("Доступ запрещён"));
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<ErrorMessage> handleIoException(IOException exception) {
        log.error(exception.getMessage(), exception);
        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(new ErrorMessage(exception.getMessage()));
    }

//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity<ErrorMessage> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
//        log.error(e.getMessage(), e);
//
//        BindingResult bindingResult = e.getBindingResult();
//        String errorMessage = bindingResult.getFieldErrors().stream()
//                .map(FieldError::getDefaultMessage)
//                .collect(Collectors.joining(", "));
//        return ResponseEntity
//                .status(HttpStatus.BAD_REQUEST)
//                .body(new ErrorMessage(errorMessage));
//    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CustomResponse<Void>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error(e.getMessage(), e);

        BindingResult bindingResult = e.getBindingResult();
        List<String> errors = bindingResult.getFieldErrors().stream()
                .map(fieldError -> "Поле " + fieldError.getField() + ": " +
                        fieldError.getDefaultMessage())
                .collect(Collectors.toList());
        return ResponseUtil.createResponse(null, false, errors);
    }
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorMessage> handleIllegalArgumentException(IllegalArgumentException exception) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorMessage(exception.getMessage()));
    }

    @ExceptionHandler(ContestStartedYetException.class)
    public ResponseEntity<ErrorMessage> handleContestStartedYetException(ContestStartedYetException exception) {
        log.error(exception.getMessage(), exception);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorMessage(exception.getMessage()));
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<CustomResponse<Void>> handleAllExceptions(Exception exception) {
        log.error(exception.getMessage(), exception);
        List<String> errors = new ArrayList<>();
        errors.add(exception.getMessage());
        return ResponseUtil.createResponse(null, false, errors);
    }
}
