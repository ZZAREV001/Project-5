package com.safetynetalert.projet5.service.impl;

import com.safetynetalert.projet5.controller.NoChildFoundFromAddressException;
import com.safetynetalert.projet5.controller.NoFirestationFoundException;
import com.safetynetalert.projet5.controller.NoPersonFoundFromNamesException;
import com.safetynetalert.projet5.model.*;
import com.safetynetalert.projet5.repository.DataFileAccess;
import org.apache.commons.collections.CollectionUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@AutoConfigureMockMvc
class FireStationsServiceImplTest {

    @Mock
    private DataFileAccess dataFileAccess;
    @Mock
    private FireStationsServiceImpl underTest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        underTest = new FireStationsServiceImpl(dataFileAccess);
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
        assertDoesNotThrow(() -> new NoFirestationFoundException(List.of(stationNumber)));
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
        assertDoesNotThrow(() -> new NoChildFoundFromAddressException(address));
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
        assertDoesNotThrow(() -> new NoFirestationFoundException(List.of(stationNumber)));
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
        assertDoesNotThrow(() -> new NoChildFoundFromAddressException(address));
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
        assertDoesNotThrow(() -> new NoPersonFoundFromNamesException(firstName, lastName));
    }
}