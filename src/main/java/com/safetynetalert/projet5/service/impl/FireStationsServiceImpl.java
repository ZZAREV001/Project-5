package com.safetynetalert.projet5.service.impl;

import com.safetynetalert.projet5.controller.NoChildFoundFromAddressException;
import com.safetynetalert.projet5.controller.NoFirestationFoundException;
import com.safetynetalert.projet5.model.*;
import com.safetynetalert.projet5.repository.DataFileAccess;
import com.safetynetalert.projet5.service.FireStationsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.commons.collections.CollectionUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FireStationsServiceImpl implements FireStationsService {

    @Autowired
    private DataFileAccess dataFileAccess;
    private MedicalRecords medicalRecords;


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

    /* TO get a list of children living at a particular address: the list should contain first name, last
       name, age and a list of the other member of the family (adults). If no children at this address: return an empty array.
           TO access children, we should traverse Medical records hash table, calculate age
           and store in a list of object.
     */
    @Override
    public ChildAlert getChildFromMedicalRecords(String address) throws IOException {
        List<FullInfoPerson> listChild = new ArrayList<>();
        List<FullInfoPerson> listAdult = new ArrayList<>();
        for (Person person : dataFileAccess.getPersonsByAddress(address)) {
            FullInfoPerson fullInfoPerson = new FullInfoPerson(person.getFirstName(), person.getLastName(),
                    null, null, null, null, null, null,
                    dataFileAccess.getAgeFromPerson(person), null, null, 0);
            if (fullInfoPerson.getAge() < 19) {
                listChild.add(fullInfoPerson);
            } else {
                listAdult.add(fullInfoPerson);
            }
        }
        if (CollectionUtils.isNotEmpty(listChild)) {
            log.info("Request get child alerts successful!");
            return new ChildAlert(address, listChild, listAdult);
        }
        log.info("Request get child alerts failed.");
        throw new NoChildFoundFromAddressException(address);


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

    private List<Integer> getStationByAddress(String address) {
        List<Integer> stationNumber = new ArrayList<>();

        for (Firestations fireStation : dataFileAccess.getFirestations()) {
            if (address.compareTo(fireStation.getAddress()) == 0) {
                stationNumber.add(fireStation.getStation());
            }
        }
        return stationNumber;
    }

    @Override
    public List<String> getCommunityEmail(String city) {
        return dataFileAccess.getPersons().stream()
                .map(Person::getEmail)
                .collect(Collectors.toList());
    }



}
