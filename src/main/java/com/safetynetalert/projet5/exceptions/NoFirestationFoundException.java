package com.safetynetalert.projet5.exceptions;

import java.util.List;

public class NoFirestationFoundException extends Throwable {

    private final List<Integer> firestationNb;

    public NoFirestationFoundException(List<Integer> firestationNb){
        super("No Firestation(s) found for number : " + firestationNb + " !");
        this.firestationNb = firestationNb;
    }

    public List<Integer> getFirestationNb() {
        return firestationNb;
    }
}
