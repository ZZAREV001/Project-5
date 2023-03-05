package com.safetynetalert.projet5.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynetalert.projet5.model.*;
import com.safetynetalert.projet5.service.FireStationsService;
import com.safetynetalert.projet5.service.impl.FireStationsServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DataControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FireStationsService fireStationsService;

    private TestRestTemplate restTemplate;

    @BeforeEach
    void setup() throws Exception {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void iTShouldGetFireStationsByID() throws Exception {
        // Given
        int stationNumber = 1;
        ObjectMapper objectMapper = new ObjectMapper();
        List<Person> personList = List.of(
                new Person("Doe", "John", "123 Main St", "Springfield", "12345", "555-1234", "john.doe@example.com"),
                new Person("Doe", "Jane", "123 Main St", "Springfield", "12345", "555-5678", "jane.doe@example.com")
        );
        long nbAdults = 2;
        long nbChildren = 0;
        FirestationsZone expectedFirestationsZone = new FirestationsZone(personList, nbAdults, nbChildren);
        given(fireStationsService.getFireStationZone(stationNumber)).willReturn(expectedFirestationsZone);

        // When
        MvcResult mvcResult = mockMvc.perform(get("/firestation")
                        .param("stationNumber", "1"))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        String expectedResponse = objectMapper.writeValueAsString(expectedFirestationsZone);
        assertThat(mvcResult.getResponse().getContentAsString()).isEqualTo(expectedResponse);
    }

    @Test
    void iTShouldGetPhoneAlert() throws Exception {
        // Given
        int stationNumber = 1;
        String expectedPhoneNumber = "841-874-6513";

        // When
        MvcResult mvcResult = mockMvc.perform(get("/phoneAlert")
                        .param("stationNumber", String.valueOf(stationNumber)))
                .andExpect(status().isOk())
                .andReturn();
        String responseJson = mvcResult.getResponse().getContentAsString();
        List<String> phoneNumberList = new ObjectMapper().readValue(responseJson, new TypeReference<>() {});

        // Then
        assertThat(phoneNumberList).contains(expectedPhoneNumber);
    }

    @Test
    void getPhoneAlertFromFireStations_shouldThrowException_whenNoFirestationFound() throws Exception {
        // Given
        int stationNumber = 99;

        // When
        mockMvc.perform(get("/phoneAlert")
                        .param("stationNumber", String.valueOf(stationNumber)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("No firestation found for station number(s): [99]"));
    }

    @Test
    void iTShouldGetCommunityEmail() throws Exception {
        String url = "/communityEmail?city=Culver";

        ResponseEntity<List<String>> responseEntity = restTemplate.exchange(
                url, HttpMethod.GET, null, new ParameterizedTypeReference<>() {});

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody()).hasSize(23);
    }

    @Test
    public void testGetFireStationsByID() {
        // Given
        int stationNumber = 1;

        // When
        ResponseEntity<FirestationsZone> responseEntity = restTemplate.getForEntity("/firestation?stationNumber=" + stationNumber, FirestationsZone.class);
        FirestationsZone firestationsZone = responseEntity.getBody();

        // Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(firestationsZone).isNotNull();
    }

    @Test
    void testGetFireAddress() {
        // Given
        String address = "1509 Culver St";

        // When
        ResponseEntity<FirePerson> response = restTemplate.getForEntity(
                "/fire?address={address}",
                FirePerson.class,
                address
        );

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        FirePerson firePerson = response.getBody();
        assertThat(firePerson).isNotNull();
    }

    @Test
    void iTShouldGetFloodStations() {
        // Given
        List<Integer> stationNumberList = Arrays.asList(1, 2, 3);

        // When
        ResponseEntity<List<InfoByStation>> response = restTemplate.exchange("/flood/stations?stationNumberList=1,2,3",
                HttpMethod.GET, null, new ParameterizedTypeReference<List<InfoByStation>>() {
                });

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        List<InfoByStation> infoByStationList = response.getBody();
        assertThat(infoByStationList).isNotNull();
        assertThat(infoByStationList).hasSizeGreaterThan(0);
    }

    @Test
    public void givenPersonInfoRequest_whenCorrectParams_thenResponseIsOk() throws Exception {
        // Given
        String firstName = "John";
        String lastName = "Doe";

        // When
        ResponseEntity<PersonInfo> response = restTemplate.getForEntity("/personInfo?firstName={firstName}&lastName={lastName}", PersonInfo.class, firstName, lastName);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }
}