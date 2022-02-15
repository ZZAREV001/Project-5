package com.safetynetalert.projet5.controller;

import java.util.List;

public class NoFloodPersonFoundException extends RuntimeException {

    private final int stationNumber;

    public NoFloodPersonFoundException(List<Integer> stationNumber, int stationNumber1) {
        super("No station number(s) found for number : " + stationNumber + " !");
        this.stationNumber = stationNumber1;
    }

    public int getStationNumber() {
        return stationNumber;
    }
}
