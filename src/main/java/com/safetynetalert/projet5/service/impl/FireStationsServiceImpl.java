package com.safetynetalert.projet5.service.impl;

import com.safetynetalert.projet5.exceptions.NoChildFoundFromAddressException;
import com.safetynetalert.projet5.exceptions.NoFirestationFoundException;
import com.safetynetalert.projet5.exceptions.NoPersonFoundFromNamesException;
import com.safetynetalert.projet5.model.*;
import com.safetynetalert.projet5.repository.DataFileAccess;
import com.safetynetalert.projet5.service.FireStationsService;
import com.safetynetalert.projet5.service.MedicalRecordsService;
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

    private final DataFileAccess dataFileAccess;

    private final MedicalRecordsService medicalRecordsService;


    @Autowired
    public FireStationsServiceImpl(DataFileAccess dataFileAccess, MedicalRecordsService medicalRecordsService) {
        this.dataFileAccess = dataFileAccess;
        this.medicalRecordsService = medicalRecordsService;
    }


    @Override
    public FirestationsZone getFireStationZone(int stationNumber) throws NoFirestationFoundException {
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
    public List<String> getPhoneAlertFromFireStations(int stationNumber) throws NoFirestationFoundException {
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

    @Override
    public List<InfoByStation> getFloodStationsForPersons(List<Integer> stations) throws NoFirestationFoundException {
        List<InfoByStation> infoByStationList = new ArrayList<>();
        int stationCounterRequest = 0;

        if (stations != null) {
            for (int stationNumber : stations) {
                List<InfoByAddress> infoByAddressList = new ArrayList<>();
                int index;

                for (Person person : dataFileAccess.getPersons()) {
                    List<Integer> stationArr = getStationByAddress(person.getAddress());
                    if (isPartOfStation(stations.get(stationCounterRequest), stationArr)) {
                        FullInfoPerson fullInfoPerson = new FullInfoPerson(
                                person.getFirstName(), person.getLastName(),
                                null, null, null, person.getPhone(), null,
                                null, dataFileAccess.getAgeFromPerson(person),
                                medicalRecordsService.getMedicationsFromPerson(person),
                                medicalRecordsService.getAllergiesFromPerson(person), 0);
                        if ((index = InfoByAddressAlreadyExist(infoByAddressList, person)) != -1) {
                            InfoByAddress infoByAddress = infoByAddressList.get(index);
                            infoByAddress.addPerson(fullInfoPerson);
                        } else {
                            List<FullInfoPerson> fullInfoPersonList = new ArrayList<>();
                            fullInfoPersonList.add(fullInfoPerson);
                            infoByAddressList.add(new InfoByAddress(person.getAddress(),
                                    fullInfoPersonList));
                        }
                    }
                }
                stationCounterRequest++;
                infoByStationList.add(new InfoByStation(infoByAddressList, stationNumber));
            }
        }
        List<Integer> nbEmptyStationList;
        if ((nbEmptyStationList = checkEmptyStation(infoByStationList)) == null) {
            log.info("Request get person information with station list successful!");
            return infoByStationList;
        }
        log.info("Request get person information with station list failed.");
        throw new NoFirestationFoundException(nbEmptyStationList);
    }

    @Override
    public PersonInfo getPersonInfo(String firstName, String lastName) {
        List<FullInfoPerson> listPersonsInfo = new ArrayList<>();

        for (Person person : dataFileAccess.getPersonsByAddressWithNames(firstName, lastName)) {
            if (person.getFirstName().equals(firstName) && person.getLastName().equals(lastName)) {
                FullInfoPerson fullInfoPerson = new FullInfoPerson(
                        person.getFirstName(),
                        person.getLastName(),
                        person.getAddress(), null, null, null, person.getEmail(),
                        null, 0,
                        medicalRecordsService.getMedicationsFromPerson(person),
                        medicalRecordsService.getAllergiesFromPerson(person), 0);
                listPersonsInfo.add(fullInfoPerson);
            }
        }
        if (CollectionUtils.isNotEmpty(listPersonsInfo)) {
            log.info("Request get persons per fire station is successful!");
            return new PersonInfo(listPersonsInfo);
        }
        log.info("Request get fire station failed.");
        throw new NoPersonFoundFromNamesException(firstName, lastName);
    }

    @Override
    public Person savePerson(Person newPerson) {
        Person result = dataFileAccess.savePerson(newPerson);
        if (result != null)
            log.info("Request save person successful: " + newPerson);
        else
            log.info("Request save person failed: " + newPerson);
        return result;
    }

    @Override
    public boolean deletePerson(Person existingPerson) {
        boolean result = dataFileAccess.deletePerson(existingPerson);
        if (result)
            log.info("Request delete person successful: " + existingPerson);
        else
            log.info("Request delete person failed: " + existingPerson);
        return result;
    }

    @Override
    public Person updatePerson(Person existingPerson) {
        Person result = dataFileAccess.updatePerson(existingPerson);
        if (result != null)
            log.info("Request update person successful: " + existingPerson);
        else
            log.info("Request update person failed: " + existingPerson);
        return result;
    }

    @Override
    public Firestations saveFirestation(Firestations newFireStations) {
        Firestations result = dataFileAccess.saveFirestation(newFireStations);
        if (result != null)
            log.info("Request save firestation successful: " + newFireStations);
        else
            log.info("Request save firestation failed: " + newFireStations);
        return result;
    }

    @Override
    public Firestations updateFireStation(Firestations existingFireStation) {
        Firestations result = dataFileAccess.updateFirestation(existingFireStation);
        if (result != null)
            log.info("Request update firestation successful: " + existingFireStation);
        else
            log.info("Request update firestation failed: " + existingFireStation);
        return result;
    }

    @Override
    public boolean deleteFireStations(Firestations existingFireStation) {
        boolean result = dataFileAccess.deleteFireStation(existingFireStation);
        if (result)
            log.info("Request delete fire station successful: " + existingFireStation);
        else
            log.info("Request delete fire station failed: " + existingFireStation);
        return result;
    }

    @Override
    public MedicalRecords saveMedicalRecords(MedicalRecords newMedicalRecords) {
        MedicalRecords result = dataFileAccess.saveMedicalRecords(newMedicalRecords);
        if (result != null)
            log.info("Request create medical record successful: " + newMedicalRecords);
        else
            log.info("Request create medical record failed: " + newMedicalRecords);
        return result;
    }

    @Override
    public MedicalRecords updateMedicalRecords(MedicalRecords existingMedicalRecords) {
        MedicalRecords result = dataFileAccess.updateMedicalRecords(existingMedicalRecords);
        if (result != null)
            log.info("Request update medical record successful: " + existingMedicalRecords);
        else
            log.info("Request update medical record failed: " + existingMedicalRecords);
        return result;
    }

    @Override
    public boolean deleteMedicalRecords(MedicalRecords existingMedicalRecords) {
        boolean result = dataFileAccess.deleteMedicalRecords(existingMedicalRecords);
        if (result)
            log.info("Request delete medical record successful: " + existingMedicalRecords);
        else
            log.info("Request delete medical record failed: " + existingMedicalRecords);
        return result;
    }

    protected boolean isPartOfStation(int station, List<Integer> stationArr) {
        for (Integer stationNumber : stationArr) {
            if (station == stationNumber)
                return true;
        }
        return false;
    }

    protected int InfoByAddressAlreadyExist(List<InfoByAddress> infoByAddressList, Person person) {
        if (infoByAddressList.size() != 0) {
            for (InfoByAddress infoByAddress : infoByAddressList) {
                if (infoByAddress.getAddress().equals(person.getAddress())) {
                    return infoByAddressList.indexOf(infoByAddress);
                }
            }
        }
        return -1;
    }

    private List<Integer> checkEmptyStation(List<InfoByStation> infoByStationList) {
        List<Integer> nbEmptyStationList = new ArrayList<>();

        if (infoByStationList != null) {
            for (InfoByStation infoByStation : infoByStationList) {
                if (CollectionUtils.isEmpty(infoByStation.getListInfo())) {
                    nbEmptyStationList.add(infoByStation.getStation());
                }
            }
            if (CollectionUtils.isNotEmpty(nbEmptyStationList)) return nbEmptyStationList;
        }
        return null;
    }

    protected List<Integer> getStationByAddress(String address) {
        List<Integer> stationNumber = new ArrayList<>();

        for (Firestations fireStation : dataFileAccess.getFirestations()) {
            if (address.compareTo(fireStation.getAddress()) == 0) {
                stationNumber.add(fireStation.getStation());
            }
        }
        return stationNumber;
    }

    // Not used helper methods, but they can be used if we extend this class:
    /*private List<String> getAddressByStation(int stationNumber) {
        List<String> fireStationAddress = new ArrayList<>();

        for (Firestations fireStation : dataFileAccess.getFirestations()) {
            if (fireStation.getStation() == stationNumber) {
                fireStationAddress.add(fireStation.getAddress());
            }
        }
        return fireStationAddress;
    }

    private List<?> getPersonFireStationMedicalInfo() {
        return Stream.of(
                        dataFileAccess.getPersons(),
                        dataFileAccess.getFirestations(),
                        dataFileAccess.getMedicalrecords())
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }*/

}
