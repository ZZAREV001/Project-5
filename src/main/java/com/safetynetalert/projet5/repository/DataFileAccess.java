package com.safetynetalert.projet5.repository;

import com.safetynetalert.projet5.model.Firestations;
import com.safetynetalert.projet5.model.FullInfoPerson;
import com.safetynetalert.projet5.model.MedicalRecords;
import com.safetynetalert.projet5.model.Person;

import java.util.List;

public interface DataFileAccess {

    int getAgeFromPerson(Person person);

    List<Person> getPersonsByFirestationNumber(int firestationNumber);

    List<Person> getPersonsByAddress(String address);

    List<Firestations> getPersonsByStation(int stationNumber);

    int getAgeFromBirthdate(String birthdate);

    int getNbStationByAddressFromPerson(Person person);

    List<String> getMedicationsPerPerson(Person person);

    List<String> getAllergiesPerPerson(Person person);

    List<Person> getPersons();

    List<Firestations> getFirestations();

    List<MedicalRecords> getMedicalrecords();

    Iterable<? extends Person> getPersonsByAddressWithNames(String firstName, String lastName);
}
