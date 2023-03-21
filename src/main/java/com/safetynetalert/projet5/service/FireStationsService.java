package com.safetynetalert.projet5.service;


import com.safetynetalert.projet5.exceptions.NoFirestationFoundException;
import com.safetynetalert.projet5.model.*;

import java.io.IOException;
import java.util.List;

public interface FireStationsService {

    FirestationsZone getFireStationZone(int firestationNumber) throws NoFirestationFoundException;

    ChildAlert getChildFromMedicalRecords(String address) throws IOException;

    List<String> getPhoneAlertFromFireStations(int firestationNumber) throws NoFirestationFoundException;

    List<String> getCommunityEmail(String city);

    FirePerson getFirePersonByAddress(String address);

    List<InfoByStation> getFloodStationsForPersons(List<Integer> stationNumberList) throws NoFirestationFoundException;

    PersonInfo getPersonInfo(String firstName, String lastName);

    Person savePerson(Person model);

    Person updatePerson(Person existingPerson);

    boolean deletePerson(Person existingPerson);

    Firestations saveFirestation(Firestations newFireStations);

    Firestations updateFireStation(Firestations existingFireStation);

    MedicalRecords saveMedicalRecords(MedicalRecords newMedicalRecords);

    MedicalRecords updateMedicalRecords(MedicalRecords existingMedicalRecords);

    boolean deleteMedicalRecords(MedicalRecords existingMedicalRecords);

    boolean deleteFireStations(Firestations existingFireStation);
}
