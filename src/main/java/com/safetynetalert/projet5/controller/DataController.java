package com.safetynetalert.projet5.controller;

import com.safetynetalert.projet5.model.Firestations;
import com.safetynetalert.projet5.model.FirestationsZone;
import com.safetynetalert.projet5.model.MedicalRecords;
import com.safetynetalert.projet5.model.Person;
import com.safetynetalert.projet5.repository.DataFileAccess;
import com.safetynetalert.projet5.service.FireStationsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class DataController {

    @Autowired
    private FireStationsService fireStationsService;

    @Autowired
    private DataFileAccess dataFileAccess;


    @GetMapping(value = "/firestation", produces = "application/json")
    public FirestationsZone getFirestationsByID(@RequestParam int stationNumber) {
        return fireStationsService.getFirestationZone(stationNumber);
    }

    @GetMapping(value = "/phoneAlert", produces = "application/json")
    public List<String> getPhoneAlert(@RequestParam int stationNumber) {
        return fireStationsService.getPhoneAlertFromFirestations(stationNumber);
    }

    @GetMapping(value = "/fire", produces = "application/json")
    public List<Integer> getStationByAddressIfFire(@RequestParam String address) {
        return fireStationsService.getStationByAddress(address);
    }

    @GetMapping(value = "/communityEmail", produces = "application/json")
    public List<String> getCommunityEmail(@RequestParam String city) {
        return fireStationsService.getCommunityEmail();
    }


    @PostMapping(value = "/firestations")
    public Firestations createFireStation(@RequestBody Firestations model) {
        return fireStationsService.saveFirestation(model);
    }

    @PutMapping(value = "/firestations")
    public Firestations updateFireStation(@RequestBody Firestations model) {
        return fireStationsService.updateFirestation(model);
    }

    @DeleteMapping(value = "/firestations")
    public boolean deleteFireStation(@RequestBody Firestations model) {
        return fireStationsService.deleteFirestation(model);
    }

    @PostMapping(value = "/person")
    public Person createNewPerson(Person model) {
        return dataFileAccess.savePerson(model);
    }

    @PutMapping(value = "/person")
    public Person updateCurrentPerson(Person model) {
        return dataFileAccess.updatePerson(model);
    }

    @DeleteMapping(value = "/person")
    public boolean deletePerson(Person model) {
        return dataFileAccess.deletePerson(model);
    }

    @PostMapping(value = "/medicalrecord")
    public MedicalRecords createNewMedicalRecords(MedicalRecords model) {
        return dataFileAccess.saveMedicalRecords(model);
    }

    @PutMapping(value = "/medicalrecord")
    public MedicalRecords updateMedicalRecords(MedicalRecords model) {
        return dataFileAccess.updateMedicalRecords(model);
    }

    @DeleteMapping(value = "/medicalrecord")
    public boolean deleteMedicalRecords(MedicalRecords model) {
        return dataFileAccess.deleteMedicalRecords(model);
    }

}
