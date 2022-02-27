package com.safetynetalert.projet5.service;


import com.safetynetalert.projet5.model.*;

import java.io.IOException;
import java.util.List;

public interface FireStationsService {

    FirestationsZone getFireStationZone(int firestationNumber);

    ChildAlert getChildFromMedicalRecords(String address) throws IOException;

    List<String> getPhoneAlertFromFireStations(int firestationNumber);

    List<String> getCommunityEmail(String city);

    FirePerson getFirePersonByAddress(String address);

    List<InfoByStation> getFloodStationsForPersons(List<Integer> stationNumberList);

    PersonInfo getPersonInfo(String firstName, String lastName);

    Person savePerson(Person model);
}
