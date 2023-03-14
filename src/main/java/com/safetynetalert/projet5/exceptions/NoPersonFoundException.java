package com.safetynetalert.projet5.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

public class NoPersonFoundException extends Throwable {

    public NoPersonFoundException(String message) {
        super(message);
    }

    @ExceptionHandler(NoPersonFoundException.class)
    public ResponseEntity<String> handleNoPersonFoundException(NoPersonFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
    }
}
