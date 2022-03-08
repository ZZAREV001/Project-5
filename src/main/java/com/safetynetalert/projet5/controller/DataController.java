package com.safetynetalert.projet5.controller;

import com.safetynetalert.projet5.model.*;
import com.safetynetalert.projet5.repository.DataFileAccess;
import com.safetynetalert.projet5.service.FireStationsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.util.List;

@RestController
public class DataController {

    @Autowired
    private FireStationsService fireStationsService;
    @Autowired
    private DataFileAccess dataFileAccess;


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
    public List<InfoByStation> getFloodStations(@RequestParam List<Integer> stationNumberList) {
        return fireStationsService.getFloodStationsForPersons(stationNumberList);
    }

    @GetMapping(value = "/personInfo", produces = "application/json")
    public PersonInfo getPersonInfo(@RequestParam String firstName, @RequestParam String lastName) {
        return fireStationsService.getPersonInfo(firstName, lastName);
    }

    @PostMapping(value = "/person")
    public Person createNewPerson(@RequestBody Person newPerson) {
        return fireStationsService.savePerson(newPerson);
    }

    @DeleteMapping(value = "/person")
    public boolean deletePerson(@RequestBody Person existingPerson) {
        return fireStationsService.deletePerson(existingPerson);
    }



}
