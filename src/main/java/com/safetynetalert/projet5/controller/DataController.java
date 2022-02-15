package com.safetynetalert.projet5.controller;

import com.safetynetalert.projet5.model.*;
import com.safetynetalert.projet5.service.FireStationsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
public class DataController {

    @Autowired
    private FireStationsService fireStationsService;


    @GetMapping(value = "/firestation", produces = "application/json")
    public FirestationsZone getFireStationsByID(@RequestParam int stationNumber) {
        return fireStationsService.getFireStationZone(stationNumber);
    }

    @GetMapping(value = "/phoneAlert", produces = "application/json")
    public List<String> getPhoneAlert(@RequestParam int stationNumber) {
        return fireStationsService.getPhoneAlertFromFireStations(stationNumber);
    }

    @GetMapping(value = "/communityEmail", produces = "application/json")
    public List<String> getCommunityEmail(@RequestParam String city) {
        return fireStationsService.getCommunityEmail(city);
    }

    @GetMapping(value = "/childAlert", produces = "application/json")
    public ChildAlert getChildAlert(@RequestParam String address) throws IOException {
        return fireStationsService.getChildFromMedicalRecords(address);
    }

    @GetMapping(value = "/fire", produces = "application/json")
    public FirePerson getFireAddress(@RequestParam String address) {
        return fireStationsService.getFirePersonByAddress(address);
    }

    @GetMapping(value = "/flood/stations", produces = "application/json")
    public List<InfoByStation> getFloodStations(@RequestBody List<Integer> stationNumberList) {
        return fireStationsService.getFloodStationsForPersons(stationNumberList);
    }



}
