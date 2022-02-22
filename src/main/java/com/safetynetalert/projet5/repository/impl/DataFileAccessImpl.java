package com.safetynetalert.projet5.repository.impl;

import com.safetynetalert.projet5.model.*;
import com.safetynetalert.projet5.repository.DataFileAccess;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

@Repository
@Slf4j
public class DataFileAccessImpl implements DataFileAccess {

    private final ObjectMapper objectMapper;
    private DataFile dataFile;

    @Autowired
    public DataFileAccessImpl(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public void setDataFile(DataFile dataFile) {
        this.dataFile = dataFile;
    }

    public DataFile loadDataFile() {
        if (dataFile != null) {
            return dataFile;
        }
        try {
            dataFile = objectMapper.readValue(
                    new File("/Users/GoldenEagle/IdeaProjects/projet-5-bis/src/main/resources/datafile.json"),
                    DataFile.class);
            log.debug("Json correctly mapped!");
        } catch (IOException e) {
            log.error("Error while JSON mapping!");
            throw new RuntimeException(e);
        }
        return dataFile;
    }

    @Override
    public int getAgeFromPerson(Person person) {
        if (person != null) {
            for (MedicalRecords medicalRecords : loadDataFile().getMedicalrecords()) {
                if (Objects.equals(person.getFirstName(), medicalRecords.getFirstName()) &&
                        Objects.equals(person.getLastName(), medicalRecords.getLastName())) {
                    return getAgeFromBirthdate(medicalRecords.getBirthdate());
                }
            }
        }
        return 0;
    }

    @Override
    public int getAgeFromBirthdate(String birthdate) {
        LocalDate currentDate = LocalDate.now();
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
            formatter = formatter.withLocale(Locale.FRANCE);
            LocalDate birthDate = LocalDate.parse(birthdate, formatter);
            return Period.between(birthDate, currentDate).getYears();
        } catch (DateTimeParseException e) {
            log.info("Birthdate non valid.");
        } catch (RuntimeException e) {
            log.info("Birthdate non valid.");
        }
        return 0;
    }

    @Override
    public List<Person> getPersonsByFirestationNumber(int fireStationNumber) {
        List<Person> result = new ArrayList<>();

        for (Person person : loadDataFile().getPersons()) {
            if (getNbStationByAddressFromPerson(person) == fireStationNumber) {
                result.add(person);
            }
        }
        return result;
    }

    @Override
    public List<Person> getPersonsByAddress(String address) {
        return getPersons().stream()
                .filter(person -> person.getAddress().contentEquals(address))
                .collect(Collectors.toList());
    }

    @Override
    public List<Firestations> getPersonsByStation(int stationNumber) {
        return getFirestations().stream()
                .filter(fireStation -> fireStation.getStation() == stationNumber)
                .collect(Collectors.toList());
    }

    @Override
    public int getNbStationByAddressFromPerson(Person person) {
        if (person != null) {
            loadDataFile();
            return getFirestations()
                    .stream()
                    .filter(fireStation -> person.getAddress().equals(fireStation.getAddress()))
                    .findFirst()
                    .map(Firestations::getStation)
                    .orElse(0);
        }
        return 0;
    }

    @Override
    public List<String> getMedicationsPerPerson(Person person) {
        for (MedicalRecords medicalRecords : getMedicalrecords()) {
            if (person.getFirstName().equals(medicalRecords.getFirstName()) &&
                    person.getLastName().equals(medicalRecords.getLastName())) {
                return medicalRecords.getMedications();
            }
        }
        return null;
    }

    @Override
    public List<String> getAllergiesPerPerson(Person person) {
        for (MedicalRecords medicalRecords : getMedicalrecords()) {
            if (person.getFirstName().equals(medicalRecords.getFirstName()) &&
                    person.getLastName().equals(medicalRecords.getLastName())) {
                return medicalRecords.getAllergies();
            }
        }
        return null;
        /* Be careful with this declarative code style:
        return Collections.singletonList(getMedicalrecords().stream()
                .filter(medicalRecords ->
                        Boolean.parseBoolean(
                                String.valueOf(person.getFirstName()
                                        .equals(medicalRecords.getFirstName()))))
                .map(MedicalRecords::getAllergies)
                .toString());*/
    }

    @Override
    public List<Person> getPersonsByAddressWithNames(String firstName, String lastName) {
        return getPersons().stream()
                .filter(person -> person.getFirstName().contentEquals(firstName) &&
                        person.getLastName().contentEquals(lastName))
                .filter(medicalRecords -> medicalRecords.getFirstName().contentEquals(firstName) &&
                        medicalRecords.getLastName().contentEquals(lastName))
                .collect(Collectors.toList());
    }

    @Override
    public List<Person> getPersons() {
        return new ArrayList<>(loadDataFile().getPersons());
    }

    @Override
    public List<Firestations> getFirestations() {
        return new ArrayList<>(loadDataFile().getFirestations());
    }

    @Override
    public List<MedicalRecords> getMedicalrecords() {
        return new ArrayList<>(loadDataFile().getMedicalrecords());
    }

    @Override
    public Person savePerson(Person model) {
        if (model != null) {
            boolean i;
            if (getPersons() != null) {
                i = getPersons().stream()
                        .noneMatch(person -> person.equals(model));
                if (i) {
                    getPersons().add(model);
                    return model;
                }
            } else {
                List<Person> personList = new ArrayList<>();
                personList.add(model);
                loadDataFile().setPersons(personList);
                return model;
            }
        }
        return null;
    }


}
