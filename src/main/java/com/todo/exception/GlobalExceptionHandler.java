package com.todo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

@ControllerAdvice
public class GlobalExceptionHandler {
    // General exception handler
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleException(Exception exception) {
        return buildResponseEntity(exception, HttpStatus.BAD_REQUEST);
    }

    // Specific exception handler
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ExceptionResponse> handleException(ResponseStatusException exception) {
        return buildResponseEntity(exception, HttpStatus.valueOf(exception.getStatusCode().value()));
    }

    private ResponseEntity<ExceptionResponse> buildResponseEntity(Exception exception, HttpStatus httpStatus) {
        ExceptionResponse error = new ExceptionResponse(httpStatus.value(), exception.getMessage(), System.currentTimeMillis());
        return new ResponseEntity<>(error, httpStatus);
    }
}
