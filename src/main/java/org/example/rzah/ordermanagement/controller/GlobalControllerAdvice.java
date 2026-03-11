package org.example.rzah.ordermanagement.controller;

import jakarta.validation.ConstraintViolationException;
import org.example.rzah.ordermanagement.dto.ExceptionHandlerDto;
import org.example.rzah.ordermanagement.exception.EmailAlreadyExistsException;
import org.example.rzah.ordermanagement.exception.ProductAlreadyExistsException;
import org.example.rzah.ordermanagement.exception.ProductNotFoundException;
import org.example.rzah.ordermanagement.exception.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

@ControllerAdvice
public class GlobalControllerAdvice {
    private static final Logger log = LoggerFactory.getLogger(GlobalControllerAdvice.class);

    @ExceptionHandler
    public ResponseEntity<ExceptionHandlerDto> handleException(ConstraintViolationException e) {
        List<String> errors = e.getConstraintViolations().stream()
                .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                .toList();
        ExceptionHandlerDto exceptionHandler = new ExceptionHandlerDto(
                HttpStatus.BAD_REQUEST.value(),
                "Validation failed for request parameters",
                errors
        );
        return new ResponseEntity<>(exceptionHandler, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ExceptionHandlerDto> handleException(UserNotFoundException e) {
        ExceptionHandlerDto exceptionHandler = new ExceptionHandlerDto(HttpStatus.NOT_FOUND.value(), e.getMessage());
        return new ResponseEntity<>(exceptionHandler, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<ExceptionHandlerDto> handleException(ProductNotFoundException e) {
        ExceptionHandlerDto exceptionHandler = new ExceptionHandlerDto(HttpStatus.NOT_FOUND.value(), e.getMessage());
        return new ResponseEntity<>(exceptionHandler, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<ExceptionHandlerDto> handleException(ProductAlreadyExistsException e) {
        ExceptionHandlerDto exceptionHandler = new ExceptionHandlerDto(HttpStatus.CONFLICT.value(), e.getMessage());
        return new ResponseEntity<>(exceptionHandler, HttpStatus.CONFLICT);
    }

    @ExceptionHandler
    public ResponseEntity<ExceptionHandlerDto> handleException(Exception e) {
        log.error("Unexpected error occurred", e);
        ExceptionHandlerDto exceptionHandler = new ExceptionHandlerDto(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "An unexpected error occurred");
        return new ResponseEntity<>(exceptionHandler, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionHandlerDto> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .toList();

        ExceptionHandlerDto exceptionHandler = new ExceptionHandlerDto(HttpStatus.BAD_REQUEST.value(),
                "Validation failed for one or more fields", errors);

        return new ResponseEntity<>(exceptionHandler, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ExceptionHandlerDto> handleException(EmailAlreadyExistsException e) {
        ExceptionHandlerDto exceptionHandler = new ExceptionHandlerDto(HttpStatus.CONFLICT.value(), e.getMessage());
        return new ResponseEntity<>(exceptionHandler, HttpStatus.CONFLICT);
    }

    @ExceptionHandler
    public ResponseEntity<ExceptionHandlerDto> handleException(HttpMessageNotReadableException e) {
        ExceptionHandlerDto exceptionHandler = new ExceptionHandlerDto(HttpStatus.BAD_REQUEST.value(), "Invalid JSON: " + e.getMessage());
        return new ResponseEntity<>(exceptionHandler, HttpStatus.BAD_REQUEST);
    }
}
