package com.safetynetalert.projet5.service;


import com.safetynetalert.projet5.model.Firestations;
import com.safetynetalert.projet5.model.FirestationsZone;
import com.safetynetalert.projet5.model.Person;

import java.util.List;

public interface FireStationsService {

    FirestationsZone getFirestationZone(int firestationNumber);

    List<String> getPhoneAlertFromFirestations(int firestationNumber);

    List<Integer> getStationByAddress(String address);

    List<String> getCommunityEmail();

    Firestations saveFirestation(Firestations model);

    Firestations updateFirestation(Firestations model);

    boolean deleteFirestation(Firestations model);
}
