package com.safetynetalert.projet5.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

public class NoPersonFoundFromAddressException extends Throwable {

    public class NoPersonFoundException extends Throwable {
        public NoPersonFoundException(String message) {
            super(message);
        }
    }
}
