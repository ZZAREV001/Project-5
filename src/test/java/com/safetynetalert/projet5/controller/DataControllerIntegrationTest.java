package com.safetynetalert.projet5.controller;

import com.safetynetalert.projet5.model.FirestationsZone;
import com.safetynetalert.projet5.model.Person;
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
import org.springframework.test.web.servlet.MockMvc;
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

@SpringBootTest
@AutoConfigureMockMvc
class DataControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FireStationsService fireStationsService;

    @BeforeEach
    void setup() throws Exception {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void iTShouldGetFireStationsByID() throws Exception {
        // Given
        int stationNumber = 3;
        Person person1 = new Person("Peter", "Duncan",
                "644 Gershwin Cir", "Culver", "97451",
                "841-874-6512", "jaboyd@email.com");
        Person person2 = new Person("Reginold", "Walker",
                "908 73rd St", "Culver", "97451",
                "841-874-8547", "reg@email.com");
        FirestationsZone firestationsZone = new FirestationsZone(Arrays.asList(person1, person2),
                3, 6);
        given(fireStationsService.getFireStationZone(stationNumber)).willReturn(firestationsZone);

        // When
        ResultActions resultActions = mockMvc.perform(get("/firestation?stationNumber=3"))
                .andExpect(status().isOk())
                .andExpect(content()
                        .string("{\"persons\":[{\"firstName\":\"Peter\",\"lastName\":\"Duncan\",\"address\":\"644 Gershwin Cir\",\"city\":\"Culver\",\"zip\":\"97451\",\"phone\":\"841-874-6512\",\"email\":\"jaboyd@email.com\"},{\"firstName\":\"Reginold\",\"lastName\":\"Walker\",\"address\":\"908 73rd St\",\"city\":\"Culver\",\"zip\":\"97451\",\"phone\":\"841-874-8547\",\"email\":\"reg@email.com\"}],\"adults\":3,\"children\":6}"));

        // Then
        assertThat(resultActions).isNotNull();
    }

    @Test
    void iTShouldGetPhoneAlert() throws Exception {
        /*// Given
        int stationNumber = 2;
        List<String> personsList = new ArrayList<>();
        BDDMockito.BDDMyOngoingStubbing<List<String>> listBDDMyOngoingStubbing =
                given(fireStationsService.getPhoneAlertFromFireStations(stationNumber))
                        .willReturn(personsList);

        // When
        ResultActions resultActions = mockMvc.perform(get("/phoneAlert?stationNumber=2"))
                .andExpect(status().isOk())
                .andExpect(content()
                        .string("[\"841-874-6513\",\"841-874-7878\",\"841-874-7512\",\"841-874-7512\",\"841-874-7458\"]"));

        // Then
        assertThat(resultActions).isEqualTo(listBDDMyOngoingStubbing);*/
    }

    @Test
    void iTShouldGetCommunityEmail() throws Exception {
        /*// Given
        String city = "Culver";
        List<String> emailList = new ArrayList<>();
        given(fireStationsService.getCommunityEmail(city)).willReturn(emailList);

        // When
        ResultActions resultActions = mockMvc.perform(get("/communityEmail?city=Culver"))
                .andExpect(status().isOk())
                .andExpect(content()
                        .string("[\"jaboyd@email.com\",\"drk@email.com\",\"tenz@email.com\",\"jaboyd@email.com\",\"jaboyd@email.com\",\"drk@email.com\",\"tenz@email.com\",\"jaboyd@email.com\",\"jaboyd@email.com\",\"tcoop@ymail.com\",\"lily@email.com\",\"soph@email.com\",\"ward@email.com\",\"zarc@email.com\",\"reg@email.com\",\"jpeter@email.com\",\"jpeter@email.com\",\"aly@imail.com\",\"bstel@email.com\",\"ssanw@email.com\",\"bstel@email.com\",\"clivfd@ymail.com\",\"gramps@email.com\"]"));
        // Then
        assertThat(resultActions).isNotNull();*/
    }

    @Test
    void iTShouldGetChildAlert() {
        // Given
        // When
        // Then
    }

    @Test
    void iTShouldGetFireAddress() {
        // Given
        // When
        // Then
    }

    @Test
    void iTShouldGetFloodStations() {
        // Given
        // When
        // Then
    }
}