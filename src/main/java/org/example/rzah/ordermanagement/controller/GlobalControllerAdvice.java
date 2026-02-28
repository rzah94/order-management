package org.example.rzah.ordermanagement.controller;

import org.example.rzah.ordermanagement.dto.ExceptionHandlerDto;
import org.example.rzah.ordermanagement.exception.EmailAlreadyExistsException;
import org.example.rzah.ordermanagement.exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalControllerAdvice {
    @ExceptionHandler
    public ResponseEntity<ExceptionHandlerDto> handleException(UserNotFoundException e) {
        ExceptionHandlerDto exceptionHandler = new ExceptionHandlerDto(e.getMessage());
        return new ResponseEntity<>(exceptionHandler, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<ExceptionHandlerDto> handleException(Exception e) {
        ExceptionHandlerDto exceptionHandler = new ExceptionHandlerDto(e.getMessage());
        return new ResponseEntity<>(exceptionHandler, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ExceptionHandlerDto> handleException(EmailAlreadyExistsException e) {
        ExceptionHandlerDto exceptionHandler = new ExceptionHandlerDto(e.getMessage());
        return new ResponseEntity<>(exceptionHandler, HttpStatus.CONFLICT);
    }

    @ExceptionHandler
    public ResponseEntity<ExceptionHandlerDto> handleException(HttpMessageNotReadableException e) {
        ExceptionHandlerDto exceptionHandler = new ExceptionHandlerDto("Invalid JSON: " + e.getMessage());
        return new ResponseEntity<>(exceptionHandler, HttpStatus.CONFLICT);
    }
}
