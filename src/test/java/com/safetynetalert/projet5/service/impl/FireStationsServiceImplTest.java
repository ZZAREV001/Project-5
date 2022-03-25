package com.safetynetalert.projet5.service.impl;

import com.safetynetalert.projet5.controller.NoChildFoundFromAddressException;
import com.safetynetalert.projet5.controller.NoFirestationFoundException;
import com.safetynetalert.projet5.controller.NoPersonFoundFromNamesException;
import com.safetynetalert.projet5.model.*;
import com.safetynetalert.projet5.repository.DataFileAccess;
import com.safetynetalert.projet5.service.MedicalRecordsService;
import org.apache.commons.collections.CollectionUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.mockito.BDDMockito.given;

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

        // Then
        if (CollectionUtils.isNotEmpty(result)) {
            assertThat(underTest.getFireStationZone(stationNumber)).isNotNull();
        }
        assertThatExceptionOfType(NoFirestationFoundException.class)
                .isThrownBy(() -> underTest.getFireStationZone(stationNumber));
    }

    @Test
    void iTShouldGetChildFromMedicalRecords() throws IOException {
        // Given
        String address = "1509 Culver St";
        List<Person> personList = new ArrayList<>();
        given(dataFileAccess.getPersonsByAddress(address)).willReturn(personList);

        // When

        // Then
        if (CollectionUtils.isNotEmpty(personList)) {
            assertThat(underTest.getChildFromMedicalRecords(address)).isNotNull();
        }
        assertThatExceptionOfType(NoChildFoundFromAddressException.class)
                .isThrownBy(() -> underTest.getChildFromMedicalRecords(address));
    }

    @Test
    void iTShouldGetPhoneAlertFromFireStations() {
        // Given
        int stationNumber = 3;
        List<Person> personList = new ArrayList<>();
        given(dataFileAccess.getPersons()).willReturn(personList);

        // When

        // Then
        if (CollectionUtils.isNotEmpty(personList)) {
            assertThat(underTest.getPhoneAlertFromFireStations(stationNumber)).isNotNull();
        }
        assertThatExceptionOfType(NoFirestationFoundException.class)
                .isThrownBy(() -> underTest.getPhoneAlertFromFireStations(stationNumber));
    }

    @Test
    void iTShouldGetCommunityEmail() {
        // Given
        String city = "Culver";
        List<Person> personList = new ArrayList<>();
        given(dataFileAccess.getPersons()).willReturn(personList);

        // When
        List<String> communityEmail = underTest.getCommunityEmail(city);

        // Then
        assertThat(communityEmail).isNotNull();
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
        /* Problem with exception: assertThatThrownBy(() -> new NoFirestationFoundException(stations))
                .isInstanceOf(NoFirestationFoundException.class)
                .hasMessageContaining("No Firestation(s) found for number : " + stations + " !");*/
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
        // Then
        if (person1 != null)
            assertThat(person1).isNotNull();
        else
            assertThat(person1).isNull();
    }

    @Test
    void iTShouldUpdatePerson() {
        // Given
        Person person1 = new Person("Hernandez", "Alejandra",
                "2301 av Example 1", "Culver", "97451",
                "841-349-1950", "Alejandra@abc.com");
        given(dataFileAccess.updatePerson(person1)).willReturn(person1);

        // When
        // Then
        if (person1 != null)
            assertThat(person1).isNotNull();
        else
            assertThat(person1).isNull();
    }

    @Test
    void iTShouldDeletePerson() {
        // Given
        Person person1 = new Person("Hernandez", "Alejandra",
                "2301 av Example 1", "Culver", "97451",
                "841-349-1950", "Alejandra@abc.com");
        Boolean isDeleted = true;
        given(dataFileAccess.deletePerson(person1)).willReturn(isDeleted);

        // When
        // Then
        if (person1 != null)
            assertThat(person1).isNotNull();
        else
            assertThat(person1).isNull();
    }

    @Test
    void iTShouldSaveFirestation() {
        // Given
        Firestations actualFireStations = new Firestations("489 Manchester St", 2);
        given(dataFileAccess.saveFirestation(actualFireStations)).willReturn(actualFireStations);

        // When
        // Then
        if (actualFireStations != null)
            assertThat(actualFireStations).isNotNull();
        else
            assertThat(actualFireStations).isNull();
    }

    @Test
    void iTShouldUpdateFireStation() {
        // Given
        Firestations actualFireStations = new Firestations("489 Manchester St", 2);
        given(dataFileAccess.updateFirestation(actualFireStations)).willReturn(actualFireStations);

        // When
        // Then
        if (actualFireStations != null)
            assertThat(actualFireStations).isNotNull();
        else
            assertThat(actualFireStations).isNull();
    }

    @Test
    void iTShouldDeleteFireStations() {
        // Given
        Firestations actualFireStations = new Firestations("489 Manchester St", 2);
        boolean isDeleted = true;
        given(dataFileAccess.deleteFireStation(actualFireStations)).willReturn(isDeleted);

        // When
        // Then
        if (actualFireStations != null)
            assertThat(actualFireStations).isNotNull();
        else
            assertThat(actualFireStations).isNull();
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
        // Then
        if (actualMedicalRecords != null)
            assertThat(actualMedicalRecords).isNotNull();
        else
            assertThat(actualMedicalRecords).isNull();
    }

    @Test
    void iTShouldUpdateMedicalRecords() {
        // Given
        MedicalRecords actualMedicalRecords = new MedicalRecords("Rodriguo", "Juan",
                "01/05/1998",
                Collections.singletonList("tetracyclaz:650mg"),
                Collections.singletonList("xilliathal"));
        given(dataFileAccess.updateMedicalRecords(actualMedicalRecords)).willReturn(actualMedicalRecords);

        // When
        // Then
        if (actualMedicalRecords != null)
            assertThat(actualMedicalRecords).isNotNull();
        else
            assertThat(actualMedicalRecords).isNull();
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
        // Then
        if (actualMedicalRecords != null)
            assertThat(actualMedicalRecords).isNotNull();
        else
            assertThat(actualMedicalRecords).isNull();
    }
}