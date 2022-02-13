package com.safetynetalert.projet5.service.impl;

import com.safetynetalert.projet5.controller.NoChildFoundFromAddressException;
import com.safetynetalert.projet5.controller.NoFirestationFoundException;
import com.safetynetalert.projet5.controller.NoFloodPersonFoundException;
import com.safetynetalert.projet5.model.*;
import com.safetynetalert.projet5.repository.DataFileAccess;
import com.safetynetalert.projet5.service.FireStationsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.commons.collections.CollectionUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FireStationsServiceImpl implements FireStationsService {

    @Autowired
    private DataFileAccess dataFileAccess;
    private MedicalRecords medicalRecords;
    private Firestations firestations;

    public FireStationsServiceImpl(DataFileAccess dataFileAccess) {
        this.dataFileAccess = dataFileAccess;
    }

    @Override
    public FirestationsZone getFireStationZone(int stationNumber) {
        List<Person> personList = dataFileAccess.getPersonsByFirestationNumber(stationNumber);
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
        throw new NoFirestationFoundException(List.of(stationNumber));
    }

    @Override
    public ChildAlert getChildFromMedicalRecords(String address) throws IOException {
        List<FullInfoPerson> listChild = new ArrayList<>();
        List<FullInfoPerson> listAdult = new ArrayList<>();
        for (Person person : dataFileAccess.getPersonsByAddress(address)) {
            FullInfoPerson fullInfoPerson = new FullInfoPerson(
                    person.getFirstName(), person.getLastName(),
                    null, null,
                    null, null,
                    null, null,
                    dataFileAccess.getAgeFromPerson(person), null,
                    null, 0);
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
    public List<String> getPhoneAlertFromFireStations(int stationNumber) {
        List<String> phoneNumberList = new ArrayList<>();

        for (Person person : dataFileAccess.getPersons()) {
            if (dataFileAccess.getNbStationByAddressFromPerson(person) == stationNumber) {
                phoneNumberList.add(person.getPhone());
            }
        }
        if (CollectionUtils.isNotEmpty(phoneNumberList)) {
            log.info("Request get phone alert successful!");
            return phoneNumberList;
        }
        log.info("Request get phone alert failed.");
        throw new NoFirestationFoundException(List.of(stationNumber));
    }

    @Override
    public List<String> getCommunityEmail(String city) {
        return dataFileAccess.getPersons().stream()
                .map(Person::getEmail)
                .collect(Collectors.toList());
    }

    @Override
    public FirePerson getFirePersonByAddress(String address) {
        List<FullInfoPerson> listPersons = new ArrayList<>();

        for (int station : getStationByAddress(address)) {
            for (Person person : dataFileAccess.getPersonsByAddress(address)) {
                FullInfoPerson fullInfoPerson = new FullInfoPerson(
                        person.getFirstName(), null,
                        person.getAddress(), null,
                        null, person.getPhone(),
                        null, null,
                        dataFileAccess.getAgeFromPerson(person), dataFileAccess.getMedicationsPerPerson(person),
                        dataFileAccess.getAllergiesPerPerson(person), dataFileAccess.getNbStationByAddressFromPerson(person));
                if (fullInfoPerson.getAddress().equals(address)) {
                    listPersons.add(fullInfoPerson);
                }
            }
            if (CollectionUtils.isNotEmpty(listPersons)) {
                log.info("Request get persons per fire station is successful!");
                return new FirePerson(listPersons);
            }
        }
        log.info("Request get fire station failed.");
        throw new NoChildFoundFromAddressException(address);
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

    private List<String> getAddressByStation(int stationNumber) {
        List<String> fireStationAddress = new ArrayList<>();

        for (Firestations fireStation : dataFileAccess.getFirestations()) {
            if (fireStation.getStation() == stationNumber) {
                fireStationAddress.add(fireStation.getAddress());
            }
        }
        return fireStationAddress;
    }

    @Override
    public FloodStation getFloodStationsForPersons(int stationNumber) {
        List<Person> personArray = dataFileAccess.getPersons() ;
        List<Firestations> fireStationsArray = dataFileAccess.getFirestations();
        ArrayList floodStation = new ArrayList<>(stationNumber);
        // if person.firstName == medicalRecords.firstName
        //     if person.address == fireStation.address
        //          traverse(personMap, medicalRecordMap, fireStationMap)
        //          create fullInfoPerson
        //          add the new fullInfoPerson to floodStationPerson object and return it
        //  else if object empty, throw a personalized flood exception.
        /* pattern: Collection collection;
           collection.forEach(name->{
                  if(name.equals(a))){
                      doSomething();
                  }
                  if(name.equals(b)){
                      doSomethingElse();
                  }
                  if(name.equals(c)){
                      doSomethingElseElse();
                  }
            });
            Remark: make 3 sub problems for personArray.forEach(), fireStationsArray and medicalRecordArray
            Then, combine the 3 to obtain a flood station object.
        */
        personArray.forEach(person -> {
            if (person.getFirstName().equals(medicalRecords.getFirstName())) {
                if (person.getAddress().equals(firestations.getAddress())) {
                    FullInfoPerson fullInfoPerson = new FullInfoPerson(
                            person.getFirstName(), null,
                            person.getAddress(), null,
                            null, person.getPhone(),
                            null, null,
                            dataFileAccess.getAgeFromPerson(person),
                            dataFileAccess.getMedicationsPerPerson(person),
                            dataFileAccess.getAllergiesPerPerson(person),0);
                    floodStation.add(fullInfoPerson);
                }
            }
            if (CollectionUtils.isNotEmpty((Collection) floodStation)) {
                log.info("Request get flood station per person is successful!");
                //return new FloodStation(floodStation);
            }
            log.info("Request get flood station per person failed.");
            //throw new NoFloodPersonFoundException(stationNumber);
        });
        return null;
    }

}
