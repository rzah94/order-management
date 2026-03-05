package org.example.rzah.ordermanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.Errors;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ExceptionHandlerDto {
    private int status;
    private String details;
    private List<String> errors = new ArrayList<>();
    private LocalDateTime date = LocalDateTime.now();

    public ExceptionHandlerDto(int status, String details) {
        this.status = status;
        this.details = details;
    }

    public ExceptionHandlerDto(int status, String details, List<String> errors) {
        this.status = status;
        this.details = details;
        this.errors = errors;
    }

    public void addError(String error) {
        errors.add(error);
    }
}
