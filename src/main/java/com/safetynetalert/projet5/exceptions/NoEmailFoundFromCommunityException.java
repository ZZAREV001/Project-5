package com.safetynetalert.projet5.exceptions;

public class NoEmailFoundFromCommunityException extends Throwable{

    public NoEmailFoundFromCommunityException(String city) {

        super("No email(s) found in this city:" + city);
    }
}

