package com.safetynetalert.projet5.service;


import com.safetynetalert.projet5.model.*;

import java.io.IOException;
import java.util.List;

public interface FireStationsService {

    FirestationsZone getFirestationZone(int firestationNumber);

    ChildAlert getChildFromMedicalRecords(String address) throws IOException;

    List<String> getPhoneAlertFromFirestations(int firestationNumber);

    List<String> getCommunityEmail(String city);

}
