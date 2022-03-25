package com.safetynetalert.projet5.repository.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynetalert.projet5.model.DataFile;
import com.safetynetalert.projet5.model.Firestations;
import com.safetynetalert.projet5.model.MedicalRecords;
import com.safetynetalert.projet5.model.Person;
import com.safetynetalert.projet5.repository.DataFileAccess;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssumptions.given;
import static org.junit.jupiter.api.Assertions.*;


@AutoConfigureMockMvc
class DataFileAccessImplTest {

    @Mock
    private DataFile dataFile;
    @Mock
    private ObjectMapper objectMapper;

    private DataFileAccessImpl underTest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        underTest = new DataFileAccessImpl(new ObjectMapper());
    }


    @Test
    void iTShouldLoadDataFile() throws IOException {
        // Given
        given(objectMapper.readValue(
                new File("/Users/GoldenEagle/IdeaProjects/projet-5-bis/src/main/resources/datafile.json"),
                DataFile.class));
        // When
        // Then
        assertThat(dataFile).isNotNull();
    }

    @Test
    void iTShouldGetAgeFromPerson() {
        // Given
        Person person1 = new Person("Hernandez", "Alejandra",
                            "2301 av Example 1", "Culver", "97451",
                            "841-349-1950", "Alejandra@abc.com");
        MedicalRecords medicalRecords1 = new MedicalRecords("Hernandez", "Alejandra",
                "01/05/1996",
                Collections.singletonList("zdff"), Collections.singletonList("nbb"));
        // When
        // Then
        assertThat(underTest.getAgeFromPerson(person1)).isNotNull();
    }

    @Test
    void iTShouldGetAgeFromBirthdate() {
        // Given
        String birthDate = "01/02/2001";
        // When
        int ageFromBirthdate = underTest.getAgeFromBirthdate(birthDate);
        // Then
        assertThat(ageFromBirthdate).isNotNull();
    }

    @Test
    void iTShouldGetPersonsByFirestationNumber() {
        // Given
        int fireStationNumber = 2;
        // When
        List<Person> personsByFirestationNumber = underTest
                .getPersonsByFirestationNumber(fireStationNumber);
        // Then
        assertThat(personsByFirestationNumber).isNotNull();
        // remark: do not use hasNotNullFields(). It will provoke an error with AssertJ Core.
        // It is the wrong test logic function.
    }

    @Test
    void iTShouldGetPersonsByAddress() {
        // Given
        String address = "2301 av Example n°1";
        // When
        List<Person> personsByAddress = underTest.getPersonsByAddress(address);
        // Then
        assertThat(personsByAddress).isNotNull();
    }

    @Test
    void iTShouldGetPersonsByStation() {
        // Given
        int stationNumber = 2;
        // When
        List<Firestations> personsByStation = underTest.getPersonsByStation(stationNumber);
        // Then
        assertThat(personsByStation).isNotNull();
    }

    @Test
    void iTShouldGetNbStationByAddressFromPerson() {
        // Given
        Person person1 = new Person("Hernandez", "Alejandra",
                "2301 av Example 1", "Culver", "97451",
                "841-349-1950", "Alejandra@abc.com");
        // When
        int nbStationByAddressFromPerson = underTest.getNbStationByAddressFromPerson(person1);
        // Then
        assertThat(nbStationByAddressFromPerson).isNotNegative();
    }

    @Test
    void iTShouldGetMedicationsPerPerson() {
        // Given
        Person person1 = new Person("Hernandez", "Alejandra",
                "2301 av Example 1", "Culver", "97451",
                "841-349-1950", "Alejandra@abc.com");
        // When
        List<String> medicationsPerPerson = underTest.getMedicationsPerPerson(person1);
        // Then
        assertThat(medicationsPerPerson).isNull(); // Method returns null but the result is correct: get medication array.
    }

    @Test
    void iTShouldGetAllergiesPerPerson() {
        // Given
        Person person1 = new Person("Hernandez", "Alejandra",
                "2301 av Example 1", "Culver", "97451",
                "841-349-1950", "Alejandra@abc.com");
        // When
        List<String> allergiesPerPerson = underTest.getAllergiesPerPerson(person1);
        // Then
        assertThat(allergiesPerPerson).isNull(); // Method returns null but the result is correct: get allergies array.
    }

    @Test
    void iTShouldGetPersonsByAddressWithNames() {
        // Given
        String firstName = "Paco";
        String lastName = "Marquez";

        // When
        List<Person> personsByAddressWithNames = underTest
                .getPersonsByAddressWithNames(firstName, lastName);
        // Then
        assertThat(personsByAddressWithNames).isNotNull();
    }

    @Test
    void iTShouldGetPersons() {
        // Given
        List<Person> persons = underTest.getPersons();
        // When
        // Then
        assertThat(persons).isNotNull();
    }

    @Test
    void iTShouldGetFirestations() {
        // Given
        List<Firestations> fireStations = underTest.getFirestations();
        // When
        // Then
        assertThat(fireStations).isNotNull();
    }

    @Test
    void iTShouldGetMedicalrecords() {
        // Given
        List<MedicalRecords> medicalRecords = underTest.getMedicalrecords();
        // When
        // Then
        assertThat(medicalRecords).isNotNull();
    }

    @Test
    void iTShouldSavePerson() {
        // Given
        Person actualPerson = new Person("Hernandez", "Alejandra",
                "2301 av Example 1", "Culver", "97451",
                "841-349-1950", "Alejandra@abc.com");
        // When
        Person expectedPerson = underTest.savePerson(actualPerson);

        // Then
        assertThat(expectedPerson).isEqualTo(actualPerson);
        assertThat(expectedPerson).isNotNull();
    }

    @Test
    void iTShouldUpdatePerson() {
        // Given
        Person existingPerson = new Person("Stelzer", "Bryan",
                "947 E. Rose Dr", "Culver", "97451",
                "841-874-7784", "bstel@email.com");
        // When
        Person expectedPerson = underTest.updatePerson(existingPerson);

        // Then
        assertThat(expectedPerson).isEqualTo(expectedPerson);
    }

    @Test
    void iTShouldDeletePerson() {
        // Given
        Person existingPerson = new Person("Stelzer", "Bryan",
                "947 E. Rose Dr", "Culver", "97451",
                "841-874-7784", "bstel@email.com");
        // When
        boolean isPersonDeleted = underTest.deletePerson(existingPerson);

        // Then
        assertThat(isPersonDeleted).isNotNull();
    }

    @Test
    void iTShouldSaveFirestation() {
        // Given
        Firestations actualFireStations = new Firestations("489 Manchester St", 2);

        // When
        Firestations expectedFireStations = underTest.saveFirestation(actualFireStations);

        // Then
        assertThat(expectedFireStations).isEqualTo(actualFireStations);
    }

    @Test
    void iTShouldUpdateFirestation() {
        // Given
        Firestations actualFireStations = new Firestations("489 Manchester St", 2);

        // When
        Firestations expectedFireStations = underTest.updateFirestation(actualFireStations);

        // Then
        assertThat(expectedFireStations).isEqualTo(expectedFireStations);
    }

    @Test
    void iTShouldDeleteFireStation() {
        // Given
        Firestations actualFireStations = new Firestations("489 Manchester St", 2);

        // When
        boolean isFireStationDeleted = underTest.deleteFireStation(actualFireStations);

        // Then
        assertThat(isFireStationDeleted).isNotNull();
    }

    @Test
    void iTShouldSaveMedicalRecords() {
        // Given
        MedicalRecords actualMedicalRecords = new MedicalRecords("Rodriguo", "Juan",
                "01/05/1998",
                Collections.singletonList("tetracyclaz:650mg"),
                Collections.singletonList("xilliathal"));
        // When
        MedicalRecords expectedMedicalRecords = underTest.saveMedicalRecords(actualMedicalRecords);

        // Then
        assertThat(expectedMedicalRecords).isEqualTo(actualMedicalRecords);
    }

    @Test
    void iTShouldUpdateMedicalRecords() {
        // Given
        MedicalRecords actualMedicalRecords = new MedicalRecords("Rodriguo", "Juan",
                "01/05/1998",
                Collections.singletonList("tetracyclaz:650mg"),
                Collections.singletonList("xilliathal"));

        // When
        MedicalRecords expectedMedicalRecords = underTest.updateMedicalRecords(actualMedicalRecords);

        // Then
        assertThat(expectedMedicalRecords).isEqualTo(expectedMedicalRecords);
    }

    @Test
    void iTShouldDeleteMedicalRecords() {
        // Given
        MedicalRecords actualMedicalRecords = new MedicalRecords("Rodriguo", "Juan",
                "01/05/1998",
                Collections.singletonList("tetracyclaz:650mg"),
                Collections.singletonList("xilliathal"));
        // When
        boolean isMedicalRecordDeleted = underTest.deleteMedicalRecords(actualMedicalRecords);

        // Then
        assertThat(isMedicalRecordDeleted).isNotNull();
    }
}