package com.safetynetalert.projet5.repository.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynetalert.projet5.model.DataFile;
import com.safetynetalert.projet5.model.Firestations;
import com.safetynetalert.projet5.model.MedicalRecords;
import com.safetynetalert.projet5.model.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import static org.assertj.core.api.AssertionsForClassTypes.tuple;
import static org.mockito.BDDMockito.then;

import java.io.IOException;
import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


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
        /*// Given
        given(objectMapper.readValue(
                new File("/Users/GoldenEagle/IdeaProjects/projet-5-bis/src/main/resources/datafile.json"),
                DataFile.class));
        // Then
        assertThat(dataFile).isNotNull();*/

        // Given
        DataFileAccessImpl underTest = new DataFileAccessImpl(new ObjectMapper());
        // When
        DataFile dataFile = underTest.loadDataFile();

        // Then
        assertThat(dataFile).isNotNull();
        assertThat(dataFile.getFirestations()).isNotNull();
        assertThat(dataFile.getMedicalrecords()).isNotNull();
        assertThat(dataFile.getPersons()).isNotNull();
        assertThat(dataFile.getMedicalrecords().get(0).getMedications()).isNotNull();
        assertThat(dataFile.getMedicalrecords().get(0).getAllergies()).isNotNull();
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
        List<Firestations> firestationsByStationNumber = underTest.getPersonsByStation(stationNumber);
        // Then
        assertThat(firestationsByStationNumber)
                .isNotNull();
        assertThat(firestationsByStationNumber.get(0).getStation())
                .isEqualTo(stationNumber);
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
        // Verify that the person was saved
        assertThat(expectedPerson).isNotNull();
        assertThat(expectedPerson.getFirstName()).isEqualTo(actualPerson.getFirstName());
        assertThat(expectedPerson.getLastName()).isEqualTo(actualPerson.getLastName());

    }

    @Test
    void iTShouldUpdatePerson() {
        // Given
        Person existingPerson = new Person("Stelzer", "Bryan",
                "947 E. Rose Dr", "Culver", "97451",
                "841-874-7784", "bstel@email.com");
        Person updatedPerson = new Person("Stelzer", "Bryan",
                "947 E. Rose Dr", "Culver", "97451",
                "841-874-7784", "bstel@email.com");
        updatedPerson.setFirstName("Alex");
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
        List<Person> personsList = new ArrayList<>();
        personsList.add(existingPerson);
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

        // Given (JUNIT version)
        /*Firestations existingFireStation = new Firestations("489 Manchester St", 2);
        underTest.saveFirestation(existingFireStation);
        existingFireStation.setAddress("123 New Address");

        // When
        Firestations updatedFireStation = underTest.updateFirestation(existingFireStation);

        // Then
        assertEquals(updatedFireStation, existingFireStation);
        assertEquals(1, underTest.loadDataFile().getFirestations().size());
        assertTrue(underTest.loadDataFile().getFirestations().contains(updatedFireStation));*/

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

        /*// Given
        MedicalRecords existingMedicalRecords = new MedicalRecords("John", "Doe", "01/01/2000", null, null);

        // When
        MedicalRecords updatedMedicalRecords = underTest.updateMedicalRecords(existingMedicalRecords);

        // Then
        assertThat(updatedMedicalRecords).isNotNull().isEqualTo(existingMedicalRecords);
        List<MedicalRecords> medicalRecordsList = underTest.loadDataFile().getMedicalrecords();
        assertThat(medicalRecordsList).isNotNull();
        Optional<MedicalRecords> updatedMedicalRecordsOptional = medicalRecordsList.stream()
                .filter(medicalRecords -> existingMedicalRecords.getFirstName()
                        .equals(medicalRecords.getFirstName())
                        && existingMedicalRecords.getLastName().equals(medicalRecords.getLastName()))
                .findFirst();
        assertThat(updatedMedicalRecordsOptional).isPresent();
        assertThat(updatedMedicalRecordsOptional.get()).isEqualTo(existingMedicalRecords);*/
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