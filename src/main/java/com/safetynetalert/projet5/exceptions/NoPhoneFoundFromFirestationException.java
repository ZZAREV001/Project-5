package com.safetynetalert.projet5.exceptions;

public class NoPhoneFoundFromFirestationException extends Throwable {

    public NoPhoneFoundFromFirestationException(int stationNumber) {
        super("No phone(s) found from firestation:" + stationNumber);
    }
}
