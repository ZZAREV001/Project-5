package com.safetynetalert.projet5.controller;

public class NoPersonFoundFromNamesException extends RuntimeException {

    private final String firstName;
    private final String lastName;

    public NoPersonFoundFromNamesException(String firstName, String lastName) {
        super("No persons found for this firstname and lastname : " + firstName + " ," +
                lastName + " !");
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }
}
