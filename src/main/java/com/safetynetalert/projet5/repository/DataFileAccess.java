package com.safetynetalert.projet5.repository;

import com.safetynetalert.projet5.model.Firestations;
import com.safetynetalert.projet5.model.MedicalRecords;
import com.safetynetalert.projet5.model.Person;

import java.util.List;

public interface DataFileAccess {

    int getAgeFromPerson(Person person);

    List<Person> getPersonsByFirestationNumber(int firestationNumber);

    int getAgeFromBirthdate(String birthdate);

    int getNbStationByAddressFromPerson(Person person);


    List<Person> getPersons();

    List<Firestations> getFirestations();

    List<MedicalRecords> getMedicalrecords();

    Person savePerson(Person model);

    Person updatePerson(Person model);

    boolean deletePerson(Person model);

    MedicalRecords saveMedicalRecords(MedicalRecords model);

    MedicalRecords updateMedicalRecords(MedicalRecords model);

    Firestations saveFirestation(Firestations model);

    Firestations updateFirestation(Firestations model);

    boolean deleteFirestation(Firestations model);

    boolean deleteMedicalRecords(MedicalRecords model);
}
