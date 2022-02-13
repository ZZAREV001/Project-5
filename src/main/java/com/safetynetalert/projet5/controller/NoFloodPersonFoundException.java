package com.safetynetalert.projet5.controller;

public class NoFloodPersonFoundException extends RuntimeException {

    private final int stationNumber;

    public NoFloodPersonFoundException(int stationNumber) {
        super("No station number(s) found for number : " + stationNumber + " !");
        this.stationNumber = stationNumber;
    }

    public int getStationNumber() {
        return stationNumber;
    }
}
