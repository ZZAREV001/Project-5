package com.safetynetalert.projet5.exceptions;

import com.safetynetalert.projet5.model.Person;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

public class NoPersonFoundException extends Throwable {

    public NoPersonFoundException(List<Person> persons) {
        super("Person(s) not found: " + persons);
    }

    @ExceptionHandler(NoPersonFoundException.class)
    public ResponseEntity<String> handleNoPersonFoundException(NoPersonFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
    }

}
