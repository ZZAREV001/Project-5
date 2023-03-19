package com.safetynetalert.projet5.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

public class NoPersonFoundFromNameException extends Throwable {

    public NoPersonFoundFromNameException() {
        super("No person(s) found from name: ");
    }

    @ExceptionHandler(com.safetynetalert.projet5.exceptions.NoPersonFoundFromNameException.class)
    public ResponseEntity<String> handleNoPersonFoundException(com.safetynetalert.projet5.exceptions.NoPersonFoundFromFirstNameAndNameException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
    }
}
