package com.safetynetalert.projet5.controller;

import com.safetynetalert.projet5.exceptions.*;
import com.safetynetalert.projet5.model.*;
import com.safetynetalert.projet5.service.FireStationsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

@RestController
public class DataController {

    private final FireStationsService fireStationsService;

    @Autowired
    public DataController(FireStationsService fireStationsService) {
        this.fireStationsService = fireStationsService;
    }


    @GetMapping(value = "/firestation", produces = "application/json")
    public FirestationsZone getFireStationsByID(@RequestParam int stationNumber) throws NoFirestationFoundException {
        FirestationsZone firestationsZone = fireStationsService.getFireStationZone(stationNumber);

        if (firestationsZone == null) {
            throw new NoFirestationFoundException(Collections.singletonList(stationNumber));
        }
        return firestationsZone;
    }

    @GetMapping(value = "/phoneAlert", produces = "application/json")
    public List<String> getPhoneAlert(@RequestParam int stationNumber) throws NoPhoneFoundFromFirestationException, NoFirestationFoundException {
        List<String> phoneAlertFromFireStations = fireStationsService.getPhoneAlertFromFireStations(stationNumber);

        if (phoneAlertFromFireStations == null) {
            throw new NoPhoneFoundFromFirestationException(stationNumber);
        }
        return phoneAlertFromFireStations;
    }

    @GetMapping(value = "/communityEmail", produces = "application/json")
    public List<String> getCommunityEmail(@RequestParam String city) throws NoEmailFoundFromCommunityException {
        List<String> communityEmail = fireStationsService.getCommunityEmail(city);

        if (communityEmail == null) {
            throw new NoEmailFoundFromCommunityException(city);
        }
        return communityEmail;
    }

    @GetMapping(value = "/childAlert", produces = "application/json")
    public ChildAlert getChildAlert(@RequestParam String address) throws IOException {
        ChildAlert childFromMedicalRecords = fireStationsService.getChildFromMedicalRecords(address);

        if (childFromMedicalRecords == null) {
            throw new NoChildFoundFromAddressException(address);
        }
        return childFromMedicalRecords;
    }

    @GetMapping(value = "/fire", produces = "application/json")
    public FirePerson getFireAddress(@RequestParam String address) throws NoFirePersonFoundException {
        FirePerson firePersonByAddress = fireStationsService.getFirePersonByAddress(address);

        if (firePersonByAddress == null) {
            throw new NoFirePersonFoundException(address);
        }
        return firePersonByAddress;
    }

    @GetMapping(value = "/flood/stations", produces = "application/json")
    public List<InfoByStation> getFloodStations(@RequestParam List<Integer> stationNumberList) throws NoFirestationFoundException {
        List<InfoByStation> floodStationsForPersons = fireStationsService.getFloodStationsForPersons(stationNumberList);

        if (floodStationsForPersons == null) {
            throw new NoFirestationFoundException(stationNumberList);
        }
        return floodStationsForPersons;
    }

    @GetMapping(value = "/personInfo", produces = "application/json")
    public PersonInfo getPersonInfo(@RequestParam String firstName, @RequestParam String lastName) throws NoPersonFoundFromFirstNameAndNameException {
        PersonInfo personInfo = fireStationsService.getPersonInfo(firstName, lastName);

        if (personInfo == null) {
            throw new NoPersonFoundFromFirstNameAndNameException(firstName, lastName);
        }
        return personInfo;
    }

    @PostMapping(value = "/person")
    public Person createNewPerson(@RequestBody Person newPerson) {
        return fireStationsService.savePerson(newPerson);
    }

    @PutMapping(value = "/person")
    public Person updatePerson(@RequestBody Person existingPerson) {
        return fireStationsService.updatePerson(existingPerson);
    }

    @DeleteMapping(value = "/person")
    public boolean deletePerson(@RequestBody Person existingPerson) {
        return fireStationsService.deletePerson(existingPerson);
    }

    @PostMapping(value = "/firestation")
    public Firestations createFireStations(@RequestBody Firestations newFireStations) {
        return fireStationsService.saveFirestation(newFireStations);
    }

    @PutMapping(value = "/firestation")
    public Firestations updateFireStations(@RequestBody Firestations existingFireStation) {
        return fireStationsService.updateFireStation(existingFireStation);
    }

    @DeleteMapping(value = "/firestation")
    public boolean deleteFireStations(@RequestBody Firestations existingFireStation) {
        return fireStationsService.deleteFireStations(existingFireStation);
    }

    @PostMapping(value = "/medicalrecords")
    public MedicalRecords createMedicalRecords(@RequestBody MedicalRecords newMedicalRecords) {
        return fireStationsService.saveMedicalRecords(newMedicalRecords);
    }

    @PutMapping(value = "/medicalrecords")
    public MedicalRecords updateMedicalRecords(@RequestBody MedicalRecords existingMedicalRecords) {
        return fireStationsService.updateMedicalRecords(existingMedicalRecords);
    }

    @DeleteMapping(value = "/medicalrecords")
    public boolean deleteMedicalRecords(@RequestBody MedicalRecords existingMedicalRecords) {
        return fireStationsService.deleteMedicalRecords(existingMedicalRecords);
    }



}
