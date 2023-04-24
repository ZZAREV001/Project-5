package com.safetynetalert.projet5.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynetalert.projet5.exceptions.NoFirestationFoundException;
import com.safetynetalert.projet5.model.*;
import com.safetynetalert.projet5.service.FireStationsService;
import com.safetynetalert.projet5.exceptions.NoChildFoundFromAddressException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.*;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DataControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FireStationsService fireStationsService;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() throws Exception {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();
    }

    @Test
    void iTShouldGetFireStationsByID() throws Exception, NoFirestationFoundException {
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
    void iTShouldGetPhoneAlert() throws Exception, NoFirestationFoundException {
        int stationNumber = 1;
        List<String> expectedResponse = Arrays.asList("123-456-7890", "234-567-8901");

        when(fireStationsService.getPhoneAlertFromFireStations(stationNumber)).thenReturn(expectedResponse);

        mockMvc.perform(get("/phoneAlert").param("stationNumber", String.valueOf(stationNumber)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(fireStationsService).getPhoneAlertFromFireStations(stationNumber);
        verifyNoMoreInteractions(fireStationsService);
    }

    @Test
    void iTShouldGetCommunityEmail() throws Exception {
        String city = "Example City";
        mockMvc.perform(MockMvcRequestBuilders.get("/communityEmail")
                        .param("city", city))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    // Problem with the test here:
    @Test
    public void getChildAlert_success() throws Exception {
        // Given
        ChildAlert expectedChildAlert = new ChildAlert("some address", Arrays.asList(
                new FullInfoPerson("lastName1", "firstName1", null, null, null, null, null,
                        null, 10, null, null, 0)),
                Arrays.asList(new FullInfoPerson("lastName2", "firstName2", null, null,
                        null, null, null, null, 30, null, null, 0)));
        when(fireStationsService.getChildFromMedicalRecords("some address"))
                .thenReturn(expectedChildAlert);

        // When
        MvcResult mvcResult = mockMvc.perform(get("/childAlert?address=some address"))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        verify(fireStationsService).getChildFromMedicalRecords("some address");
        Assertions.assertThat(mvcResult.getResponse().getContentAsString())
                .isEqualTo(objectMapper.writeValueAsString(expectedChildAlert));
    }

    @Test
    public void getChildAlert_noChildFound() throws Exception {
        String address = "testAddress";
        when(fireStationsService.getChildFromMedicalRecords(address)).thenThrow(new NoChildFoundFromAddressException(address));

        mockMvc.perform(get("/childAlert")
                        .param("address", address)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void iTShouldGetFireAddress() throws Exception {
        // Setup
        String address = "123 Main St";
        List<FullInfoPerson> newPerson = new ArrayList<>();
        FirePerson expectedResponse = new FirePerson(newPerson);
        given(fireStationsService.getFirePersonByAddress(address)).willReturn(expectedResponse);

        // Perform and Assert
        mockMvc.perform(get("/fire")
                        .param("address", address)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    void iTShouldGetFloodStations() throws Exception, NoFirestationFoundException {
        // Given
        List<InfoByStation> expectedResponse = new ArrayList<>();
        expectedResponse.add(new InfoByStation(1, new ArrayList<>()));
        expectedResponse.add(new InfoByStation(2, new ArrayList<>()));
        List<Integer> stationNumberList = Arrays.asList(1, 2);

        // When
        when(fireStationsService.getFloodStationsForPersons(stationNumberList)).thenReturn(expectedResponse);

        // Then
        mockMvc.perform(get("/flood/stations")
                        .param("stationNumberList", "1", "2"))
                .andExpect(status().isOk());
                //.andExpect(content().json("[{\"station\": 1, \"persons\": []}, {\"station\": 2, \"persons\": []}]"));
    }

    @Test
    public void iTShouldGetPersonInfo() throws Exception {
        // Arrange
        String firstName = "John";
        String lastName = "Doe";
        PersonInfo personInfo = new PersonInfo();
        Mockito.when(fireStationsService.getPersonInfo(firstName, lastName)).thenReturn(personInfo);

        // Act and assert
        mockMvc.perform(MockMvcRequestBuilders.get("/personInfo")
                        .param("firstName", firstName)
                        .param("lastName", lastName)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(personInfo)));
    }

    @Test
    public void iTShouldCreateNewPerson() throws Exception {
        Person newPerson = new Person();
        newPerson.setFirstName("John");
        newPerson.setLastName("Doe");

        Mockito.when(fireStationsService.savePerson(Mockito.any())).thenReturn(newPerson);

        mockMvc.perform(MockMvcRequestBuilders.post("/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\": \"John\", \"lastName\": \"Doe\"}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"));
    }

    @Test
    public void iTShouldUpdatePerson() throws Exception {
        // Arrange
        Person existingPerson = new Person();
        existingPerson.setFirstName("John");
        existingPerson.setLastName("Doe");
        existingPerson.setAddress("123 Main St");
        existingPerson.setCity("Springfield");
        existingPerson.setZip("12345");
        existingPerson.setPhone("555-1234");
        existingPerson.setEmail("john.doe@example.com");

        when(fireStationsService.updatePerson(existingPerson)).thenReturn(existingPerson);

        // Act
        ResponseEntity<Person> response = restTemplate.exchange("/person", HttpMethod.PUT,
                new HttpEntity<>(existingPerson), Person.class);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(existingPerson);

        verify(fireStationsService).updatePerson(existingPerson);
    }

    @Test
    public void iTShouldDeletePerson() throws Exception {
        mockMvc.perform(delete("/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\":\"John\",\"lastName\":\"Doe\",\"address\":\"123 Main St\",\"city\":\"Anytown\",\"zip\":\"12345\",\"phone\":\"111-111-1111\",\"email\":\"johndoe@example.com\"}"))
                .andExpect(status().isOk());
        verify(fireStationsService).deletePerson(any(Person.class));
    }

    @Test
    public void iTShouldCreateFireStationsTest() throws Exception {
        Firestations newFirestation = new Firestations("123 Main St", 1);
        String requestBody = new ObjectMapper().writeValueAsString(newFirestation);

        when(fireStationsService.saveFirestation(newFirestation)).thenReturn(newFirestation);

        mockMvc.perform(MockMvcRequestBuilders.post("/firestation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        verify(fireStationsService, times(1)).saveFirestation(newFirestation);
    }

    @Test
    public void iTShouldUpdateFireStation() throws Exception {
        // Create a new Firestations object with the updated data
        Firestations existingFireStation = new Firestations("123 Main St", 2);

        // Convert the Firestations object to JSON
        String requestBody = new ObjectMapper().writeValueAsString(existingFireStation);

        // Set up the MockMvc environment
        mockMvc.perform(MockMvcRequestBuilders.put("/firestation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isOk());

        // Verify that the update method was called once with the correct object
        verify(fireStationsService, times(1)).updateFireStation(existingFireStation);
    }

    @Test
    public void iTShouldDeleteFireStation() throws Exception {
        Firestations existingFirestation = new Firestations("123 Main St", 1);
        String requestBody = new ObjectMapper().writeValueAsString(existingFirestation);

        doReturn(true).when(fireStationsService).deleteFireStations(existingFirestation);

        mockMvc.perform(delete("/firestation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isOk());

        verify(fireStationsService, times(1)).deleteFireStations(existingFirestation);
    }

    @Test
    public void itShouldCreateMedicalRecords() throws Exception {
        MedicalRecords medicalRecords = new MedicalRecords("John", "Doe", "01/01/1970", new ArrayList<>(), new ArrayList<>());
        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper.writeValueAsString(medicalRecords);

        mockMvc.perform(MockMvcRequestBuilders.post("/medicalrecords")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isOk());

        verify(fireStationsService, times(1)).saveMedicalRecords(medicalRecords);
    }

    @Test
    public void itShouldUpdateMedicalRecords() throws Exception {
        MedicalRecords medicalRecords = new MedicalRecords("John", "Doe", "01/01/1970", new ArrayList<>(), new ArrayList<>());
        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper.writeValueAsString(medicalRecords);

        mockMvc.perform(MockMvcRequestBuilders.put("/medicalrecords")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isOk());

        verify(fireStationsService, times(1)).updateMedicalRecords(medicalRecords);
    }

    @Test
    public void itShouldDeleteMedicalRecords() throws Exception {
        MedicalRecords medicalRecords = new MedicalRecords("John", "Doe", "01/01/1970", new ArrayList<>(), new ArrayList<>());
        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper.writeValueAsString(medicalRecords);

        mockMvc.perform(delete("/medicalrecords")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isOk());

        verify(fireStationsService, times(1)).deleteMedicalRecords(medicalRecords);
    }



}