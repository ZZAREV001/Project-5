package com.safetynetalert.projet5.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

public class NoPersonFoundFromFirstNameAndNameException extends Throwable {

        public NoPersonFoundFromFirstNameAndNameException(String firstName, String lastName) {
            super("No person(s) found from firstname:" + firstName +  "and name:" + lastName);
        }
}

