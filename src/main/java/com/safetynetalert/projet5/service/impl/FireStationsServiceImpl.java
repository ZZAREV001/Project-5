package com.safetynetalert.projet5.service.impl;

import com.safetynetalert.projet5.controller.NoFirestationFoundException;
import com.safetynetalert.projet5.exceptions.ControllerAdvisor;
import com.safetynetalert.projet5.model.Firestations;
import com.safetynetalert.projet5.model.FirestationsZone;
import com.safetynetalert.projet5.model.Person;
import com.safetynetalert.projet5.repository.DataFileAccess;
import com.safetynetalert.projet5.service.FireStationsService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FireStationsServiceImpl implements FireStationsService {

    private static final Logger log = LogManager.getLogger(ControllerAdvisor.class);

    @Autowired
    private DataFileAccess dataFileAccess;


    @Override
    public FirestationsZone getFirestationZone(int firestationNumber) {
        List<Person> personList = dataFileAccess.getPersonsByFirestationNumber(firestationNumber);
        long nbChildren = personList.stream()
                .filter(p -> dataFileAccess.getAgeFromPerson(p) <= 18)
                .count();
        long nbAdults = personList.stream()
                .filter(p -> dataFileAccess.getAgeFromPerson(p) > 18)
                .count();
        if (CollectionUtils.isNotEmpty(personList)) {
            log.info("Request get firestation zone successful!");
            return new FirestationsZone(personList, nbAdults, nbChildren);
        }
        log.info("Request get firestation zone failed.");
        throw new NoFirestationFoundException(List.of(firestationNumber));
    }

    @Override
    public List<String> getPhoneAlertFromFirestations(int firestationNumber) {
        List<String> phoneNumberList = new ArrayList<>();

        for (Person person : dataFileAccess.getPersons()) {
            if (dataFileAccess.getNbStationByAddressFromPerson(person) == firestationNumber) {
                phoneNumberList.add(person.getPhone());
            }
        }
        if (CollectionUtils.isNotEmpty(phoneNumberList)) {
            log.info("Request get phone alert successful!");
            return phoneNumberList;
        }
        log.info("Request get phone alert failed.");
        throw new NoFirestationFoundException(List.of(firestationNumber));
    }

    @Override
    public List<Integer> getStationByAddress(String address) {
        List<Integer> stationNumber = new ArrayList<>();

        for (Firestations fireStation : dataFileAccess.getFirestations()) {
            if (address.compareTo(fireStation.getAddress()) == 0) {
                stationNumber.add(fireStation.getStation());
            }
        }
        return stationNumber;
    }

    @Override
    public List<String> getCommunityEmail() {
        return dataFileAccess.getPersons().stream()
                .map(Person::getEmail)
                .collect(Collectors.toList());
    }



    @Override
    public Firestations saveFirestation(Firestations model) {
        Firestations result = dataFileAccess.saveFirestation(model);
        if (result != null) log.info("Request save firestation successful!");
        log.info("Request save firestation failed.");
        return result;
    }

    @Override
    public Firestations updateFirestation(Firestations model) {
        Firestations result = dataFileAccess.updateFirestation(model);
        if (result != null) log.info("Request save firestation successful!");
        log.info("Request save firestation failed.");
        return result;
    }

    @Override
    public boolean deleteFirestation(Firestations model) {
        boolean result = dataFileAccess.deleteFirestation(model);
        if (result) log.info("Request delete firestation successful!");
        log.info("Request delete firestation failed.");
        return result;
    }

}
