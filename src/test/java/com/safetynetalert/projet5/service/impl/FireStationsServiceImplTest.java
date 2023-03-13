package com.safetynetalert.projet5.service.impl;

import com.safetynetalert.projet5.controller.NoChildFoundFromAddressException;
import com.safetynetalert.projet5.controller.NoFirestationFoundException;
import com.safetynetalert.projet5.controller.NoPersonFoundFromNamesException;
import com.safetynetalert.projet5.model.*;
import com.safetynetalert.projet5.repository.DataFileAccess;
import com.safetynetalert.projet5.service.FireStationsService;
import com.safetynetalert.projet5.service.MedicalRecordsService;
import org.apache.commons.collections.CollectionUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@AutoConfigureMockMvc
class FireStationsServiceImplTest {

    @Mock
    private DataFileAccess dataFileAccess;
    @Mock
    private FireStationsServiceImpl underTest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        underTest = new FireStationsServiceImpl(dataFileAccess, null);
    }

    @Test
    void iTShouldGetFireStationZone() {
        // Given
        int stationNumber = 2;
        int age = 25;
        List<Person> result = new ArrayList<>();
        Person person1 = new Person("Hernandez", "Alejandra",
                "2301 av Example 1", "Culver", "97451",
                "841-349-1950", "Alejandra@abc.com");
        given(dataFileAccess.getPersonsByFirestationNumber(stationNumber)).willReturn(result);
        given(dataFileAccess.getAgeFromPerson(person1)).willReturn(age);

        // When
        result.add(person1);
        FirestationsZone fireStationZone = underTest.getFireStationZone(stationNumber);

        // Then
        /*if (CollectionUtils.isNotEmpty(result)) {
            assertThat(underTest.getFireStationZone(stationNumber)).isNotNull();
        }
        assertThatExceptionOfType(NoFirestationFoundException.class)
                .isThrownBy(() -> underTest.getFireStationZone(stationNumber));*/

        assertThat(fireStationZone).isNotNull();
        assertThat(fireStationZone.getPersons()).containsExactly(person1);
        assertThat(fireStationZone.getAdults()).isEqualTo(1);
        assertThat(fireStationZone.getChildren()).isEqualTo(0);
    }

    @Test
    void iTShouldGetChildFromMedicalRecords() throws IOException {
        // Given
        String address = "1509 Culver St";
        //List<Person> personList = new ArrayList<>();
        Person adult = new Person("Doe", "John", address, "Culver", "97451", "841-349-1950", "john.doe@abc.com");
        Person child = new Person("Doe", "Jane", address, "Culver", "97451", "841-349-1950", "jane.doe@abc.com");
        MedicalRecords medicalRecords = new MedicalRecords("Jane", "Doe", "01/01/2010", List.of("med1", "med2"), List.of("allergy1"));
        given(dataFileAccess.getPersonsByAddress(address)).willReturn(List.of(adult, child));
        given(dataFileAccess.getMedicalrecords()).willReturn(List.of(medicalRecords));
        //given(dataFileAccess.getPersonsByAddress(address)).willReturn(personList);

        // When
        ChildAlert childAlert = underTest.getChildFromMedicalRecords(address);

        // Then
        assertThat(childAlert).isNotNull();
        assertThat(childAlert.getAddress()).isEqualTo(address);
        assertThat(childAlert.getChild().get(0).getFirstName()).isEqualTo("Doe");
        assertThat(childAlert.getChild().get(0).getLastName()).isEqualTo("John");
        /*if (CollectionUtils.isNotEmpty(personList)) {
            assertThat(underTest.getChildFromMedicalRecords(address)).isNotNull();
        }
        assertThatExceptionOfType(NoChildFoundFromAddressException.class)
                .isThrownBy(() -> underTest.getChildFromMedicalRecords(address));*/
    }

    @Test
    void iTShouldGetPhoneAlertFromFireStations() {
        // Given
        int stationNumber = 3;
        List<Person> personList = new ArrayList<>();
        personList.add(new Person("Doe", "John", "123 Main St", "Anytown", "12345", "555-1234", "jdoe@example.com"));
        given(dataFileAccess.getPersons()).willReturn(personList);
        given(dataFileAccess.getNbStationByAddressFromPerson(personList.get(0))).willReturn(stationNumber);

        // When
        List<String> phoneNumbers = underTest.getPhoneAlertFromFireStations(stationNumber);

        // Then
        assertThat(underTest.getPhoneAlertFromFireStations(stationNumber)).isNotNull();
        assertThat(phoneNumbers).containsOnly(personList.get(0).getPhone());
    }

    @Test
    void iTShouldGetCommunityEmail() {
        // Given
        String city = "Culver";
        List<Person> personList = new ArrayList<>();
        personList.add(new Person("Doe", "John", "123 Main St", "Culver", "12345", "555-1234", "johndoe@example.com"));
        personList.add(new Person("Smith", "Jane", "456 Elm St", "Culver", "12345", "555-5678", "janesmith@example.com"));
        given(dataFileAccess.getPersons()).willReturn(personList);

        // When
        List<String> communityEmail = underTest.getCommunityEmail(city);

        // Then
        assertThat(communityEmail).isNotNull();
        assertThat(communityEmail)
                .isNotNull()
                .hasSize(2)
                .contains("johndoe@example.com", "janesmith@example.com");
    }

    @Test
    void iTShouldGetFirePersonByAddress() {
        // Given
        String address = "1509 Culver St";
        List<Person> personList = new ArrayList<>();
        given(dataFileAccess.getPersonsByAddress(address)).willReturn(personList);

        // When

        // Then
        if (CollectionUtils.isNotEmpty(personList)) {
            FirePerson firePersonByAddress = underTest.getFirePersonByAddress(address);
            assertThat(firePersonByAddress).isNotNull();
            assertThat(firePersonByAddress.getPersons()).hasSize(0);
        }
        assertThatExceptionOfType(NoChildFoundFromAddressException.class)
                .isThrownBy(() -> underTest.getFirePersonByAddress(address));
    }

    @Test
    void iTShouldGetFloodStationsForPersons() {
        // Given
        List<Integer> stations = new ArrayList<>();
        List<Person> personsList = new ArrayList<>();
        given(dataFileAccess.getPersons()).willReturn(personsList);

        // When
        List<InfoByStation> floodStationsForPersons = underTest.getFloodStationsForPersons(stations);

        // Then
        assertThat(floodStationsForPersons).isNotNull();
        assertThat(floodStationsForPersons).hasSize(0);

        /*List<Person> testPersons = Arrays.asList(
                new Person("John", "Doe", "123 Main St", "Anytown", "12345", "555-1234", "john.doe@example.com"),
                new Person("Jane", "Doe", "456 Oak St", "Othertown", "67890", "555-5678", "jane.doe@example.com")
        );

        when(dataFileAccess.getPersons()).thenReturn(testPersons);

        List<Integer> stations = Arrays.asList(1, 2);
        List<InfoByStation> expected = Arrays.asList(
                new InfoByStation(Arrays.asList(
                        new InfoByAddress("123 Main St", List.of(
                                new FullInfoPerson("John", "Doe", "123 Main St", "Anytown", "12345", "555-1234", "john.doe@example.com", null, 0, null, null, 0)
                        )),
                        new InfoByAddress("456 Oak St", List.of(
                                new FullInfoPerson("Jane", "Doe", "456 Oak St", "Othertown", "67890", "555-5678", "jane.doe@example.com", null, 0, null, null, 0)
                        ))
                ), 1),
                new InfoByStation(Arrays.asList(
                        new InfoByAddress("123 Main St", List.of(
                                new FullInfoPerson("John", "Doe", "123 Main St", "Anytown", "12345", "555-1234", "john.doe@example.com", null, 0, null, null, 0)
                        )),
                        new InfoByAddress("456 Oak St", List.of(
                                new FullInfoPerson("Jane", "Doe", "456 Oak St", "Othertown", "67890", "555-5678", "jane.doe@example.com", null, 0, null, null, 0)
                        ))
                ), 2)
        );

        List<InfoByStation> actual = underTest.getFloodStationsForPersons(stations);

        assertThat(actual).isEqualTo(expected);*/

    }

    @Test
    void iTShouldGetPersonInfo() {
        // Given
        String firstName = "abvx";
        String lastName = "bsbd";
        List<FullInfoPerson> personInfo = new ArrayList<>();
        List<Person> personsByAddressWithNames = (List<Person>) dataFileAccess
                .getPersonsByAddressWithNames(firstName, lastName);
        List<Person> personInfoList = new ArrayList<>();
        given(personsByAddressWithNames).willReturn(personInfoList);

        // When
        if (CollectionUtils.isNotEmpty(personInfo)) {
            underTest.getPersonInfo(firstName, lastName);
        }

        // Then
        assertThat(personInfo).isNotNull();
        assertThatExceptionOfType(NoPersonFoundFromNamesException.class)
                .isThrownBy(() -> underTest.getPersonInfo(firstName, lastName));
    }

    @Test
    void iTShouldSavePerson() {
        // Given
        Person person1 = new Person("Hernandez", "Alejandra",
                "2301 av Example 1", "Culver", "97451",
                "841-349-1950", "Alejandra@abc.com");
        given(dataFileAccess.savePerson(person1)).willReturn(person1);

        // When
        Person savedPerson = underTest.savePerson(person1);

        // Then
        assertThat(savedPerson).isNotNull();
        assertThat(savedPerson.getFirstName()).isEqualTo(person1.getFirstName());
        assertThat(savedPerson.getLastName()).isEqualTo(person1.getLastName());
        assertThat(savedPerson.getAddress()).isEqualTo(person1.getAddress());
        assertThat(savedPerson.getCity()).isEqualTo(person1.getCity());
        assertThat(savedPerson.getZip()).isEqualTo(person1.getZip());
        assertThat(savedPerson.getPhone()).isEqualTo(person1.getPhone());
        assertThat(savedPerson.getEmail()).isEqualTo(person1.getEmail());
    }

    @Test
    void iTShouldUpdatePerson() {
        // Given
        Person existingPerson = new Person("Hernandez", "Alejandra",
                "2301 av Example 1", "Culver", "97451",
                "841-349-1950", "Alejandra@abc.com");
        given(dataFileAccess.updatePerson(existingPerson)).willReturn(existingPerson);

        // When
        Person updatedPerson = underTest.updatePerson(existingPerson);

        // Then
        assertThat(updatedPerson).isNotNull();
        assertThat(updatedPerson.getFirstName()).isEqualTo(existingPerson.getFirstName());
        assertThat(updatedPerson.getLastName()).isEqualTo(existingPerson.getLastName());
        assertThat(updatedPerson.getAddress()).isEqualTo(existingPerson.getAddress());
        assertThat(updatedPerson.getCity()).isEqualTo(existingPerson.getCity());
        assertThat(updatedPerson.getZip()).isEqualTo(existingPerson.getZip());
        assertThat(updatedPerson.getPhone()).isEqualTo(existingPerson.getPhone());
        assertThat(updatedPerson.getEmail()).isEqualTo(existingPerson.getEmail());
    }

    @Test
    void iTShouldDeletePerson() {
        // Given
        Person existingPerson = new Person("Hernandez", "Alejandra",
                "2301 av Example 1", "Culver", "97451",
                "841-349-1950", "Alejandra@abc.com");
        given(dataFileAccess.deletePerson(existingPerson)).willReturn(true);

        // When
        boolean result = underTest.deletePerson(existingPerson);

        // Then
        assertThat(result).isTrue();
    }

    @Test
    void iTShouldSaveFirestation() {
        // Given
        Firestations newFireStations = new Firestations("489 Manchester St", 2);
        given(dataFileAccess.saveFirestation(newFireStations)).willReturn(newFireStations);

        // When
        Firestations result = underTest.saveFirestation(newFireStations);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getAddress()).isEqualTo("489 Manchester St");
        assertThat(result.getStation()).isEqualTo(2);
    }

    @Test
    void iTShouldUpdateFireStation() {
        // Given
        Firestations existingFireStation = new Firestations("123 Test St", 1);
        given(dataFileAccess.updateFirestation(existingFireStation)).willReturn(existingFireStation);

        // When
        Firestations updatedFireStation = underTest.updateFireStation(existingFireStation);

        // Then
        assertThat(updatedFireStation).isNotNull();
        assertThat(updatedFireStation.getAddress()).isEqualTo(existingFireStation.getAddress());
        assertThat(updatedFireStation.getStation()).isEqualTo(existingFireStation.getStation());
    }

    @Test
    void iTShouldDeleteFireStations() {
        // Given
        Firestations fireStation = new Firestations("123 Main St", 1);
        given(dataFileAccess.deleteFireStation(fireStation)).willReturn(true);

        // When
        boolean result = underTest.deleteFireStations(fireStation);

        // Then
        assertThat(result).isTrue();
        verify(dataFileAccess).deleteFireStation(fireStation);
    }

    @Test
    void iTShouldSaveMedicalRecords() {
        // Given
        MedicalRecords actualMedicalRecords = new MedicalRecords("Rodriguo", "Juan",
                "01/05/1998",
                Collections.singletonList("tetracyclaz:650mg"),
                Collections.singletonList("xilliathal"));
        given(dataFileAccess.saveMedicalRecords(actualMedicalRecords)).willReturn(actualMedicalRecords);

        // When
        MedicalRecords savedMedicalRecords = underTest.saveMedicalRecords(actualMedicalRecords);

        // Then
        assertThat(savedMedicalRecords).isNotNull();
        assertThat(savedMedicalRecords.getFirstName()).isEqualTo(actualMedicalRecords.getFirstName());
        assertThat(savedMedicalRecords.getLastName()).isEqualTo(actualMedicalRecords.getLastName());
        assertThat(savedMedicalRecords.getBirthdate()).isEqualTo(actualMedicalRecords.getBirthdate());
        assertThat(savedMedicalRecords.getMedications()).containsExactlyElementsOf(actualMedicalRecords.getMedications());
        assertThat(savedMedicalRecords.getAllergies()).containsExactlyElementsOf(actualMedicalRecords.getAllergies());
    }

    @Test
    void iTShouldUpdateMedicalRecords() {
        // Given
        MedicalRecords existingMedicalRecords = new MedicalRecords("Rodriguo", "Juan",
                "01/05/1998",
                Collections.singletonList("tetracyclaz:650mg"),
                Collections.singletonList("xilliathal"));
        given(dataFileAccess.updateMedicalRecords(existingMedicalRecords)).willReturn(existingMedicalRecords);

        // When
        MedicalRecords result = underTest.updateMedicalRecords(existingMedicalRecords);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getFirstName()).isEqualTo(existingMedicalRecords.getFirstName());
        assertThat(result.getLastName()).isEqualTo(existingMedicalRecords.getLastName());
        assertThat(result.getBirthdate()).isEqualTo(existingMedicalRecords.getBirthdate());
        assertThat(result.getMedications()).isEqualTo(existingMedicalRecords.getMedications());
        assertThat(result.getAllergies()).isEqualTo(existingMedicalRecords.getAllergies());
    }

    @Test
    void iTShouldDeleteMedicalRecords() {
        // Given
        MedicalRecords actualMedicalRecords = new MedicalRecords("Rodriguo", "Juan",
                "01/05/1998",
                Collections.singletonList("tetracyclaz:650mg"),
                Collections.singletonList("xilliathal"));
        boolean isDeleted = true;
        given(dataFileAccess.deleteMedicalRecords(actualMedicalRecords)).willReturn(isDeleted);

        // When
        boolean result = underTest.deleteMedicalRecords(actualMedicalRecords);

        // Then
        assertThat(result).isTrue();
        verify(dataFileAccess).deleteMedicalRecords(actualMedicalRecords);
    }

    @Test
    void itShouldReturnTrueIfStationIsPartOfList() {
        // Given
        int station = 2;
        List<Integer> stationArr = Arrays.asList(1, 2, 3);

        // When
        boolean result = underTest.isPartOfStation(station, stationArr);

        // Then
        assertThat(result).isTrue();
    }

    @Test
    void itShouldReturnFalseIfStationIsNotPartOfList() {
        // Given
        int station = 4;
        List<Integer> stationArr = Arrays.asList(1, 2, 3);

        // When
        boolean result = underTest.isPartOfStation(station, stationArr);

        // Then
        assertThat(result).isFalse();
    }

    @Test
    void testInfoByAddressAlreadyExist() {
        // Given
        List<InfoByAddress> infoByAddressList = new ArrayList<>();
        Person person = new Person("John", "Doe", "123 Main St", "Anytown", "12345", "555-1234", "johndoe@example.com");
        infoByAddressList.add(new InfoByAddress("123 Main St", new ArrayList<>()));
        infoByAddressList.add(new InfoByAddress("456 Oak St", new ArrayList<>()));
        infoByAddressList.add(new InfoByAddress("789 Pine St", new ArrayList<>()));

        // When
        int index = underTest.InfoByAddressAlreadyExist(infoByAddressList, person);

        // Then
        assertThat(index).isEqualTo(0);
    }

    @Test
    void testGetStationByAddress() {
        // Given
        String address = "1509 Culver St";
        List<Firestations> firestationsList = List.of(
                new Firestations("1509 Culver St", 3),
                new Firestations("5 Street", 2),
                new Firestations("10 Street", 3),
                new Firestations("15 Street", 1)
        );
        given(dataFileAccess.getFirestations()).willReturn(firestationsList);

        // When
        List<Integer> result = underTest.getStationByAddress(address);

        // Then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(3);
    }









}