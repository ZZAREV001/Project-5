package com.safetynetalert.projet5.exceptions;

public class NoFirePersonFoundException extends Throwable {

    public NoFirePersonFoundException(String message) {
        super();
    }

    public String getMessage() {
        return "No person found from address!";
    }
}
