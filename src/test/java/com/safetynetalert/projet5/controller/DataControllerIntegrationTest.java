package com.safetynetalert.projet5.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynetalert.projet5.model.*;
import com.safetynetalert.projet5.service.FireStationsService;
import com.safetynetalert.projet5.service.impl.FireStationsServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
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
import static org.assertj.core.internal.bytebuddy.matcher.ElementMatchers.is;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.mock.http.server.reactive.MockServerHttpRequest.post;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


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

    @Test
    void itTShouldGetChildAlert() throws Exception {
        String address = "123 Main St";

        // perform GET request and expect status 200 OK
        mockMvc.perform(get("/childAlert")
                        .param("address", address))
                .andExpect(status().isOk());
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
    void iTShouldGetFloodStations() throws Exception {
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
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("John"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value("Doe"));
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
        Person personToDelete = new Person();
        personToDelete.setFirstName("John");
        String personJson = new ObjectMapper().writeValueAsString(personToDelete);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.delete("/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(personJson))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String responseJson = mvcResult.getResponse().getContentAsString();
        Boolean result = new ObjectMapper().readValue(responseJson, Boolean.class);

        assertTrue(result);
    }

    @Test
    public void iTShouldCreateFireStationsTest() throws Exception {
        Firestations fireStations = new Firestations();
        fireStations.setStation(1);
        fireStations.setAddress("123 Main St");

        when(fireStationsService.saveFirestation(any(Firestations.class))).thenReturn(fireStations);

        mockMvc.perform(post("/firestation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"station\": 1, \"address\": \"123 Main St\" }")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.station").value(1))
                .andExpect(jsonPath("$.address").value("123 Main St"));

        verify(fireStationsService, times(1)).saveFirestation(any(Firestations.class));
    }

    public static String asJsonString(final Object obj) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            final String jsonContent = mapper.writeValueAsString(obj);
            return jsonContent;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testUpdateFireStation() {
        // Create a new Firestation object
        Firestations newFirestation = new Firestations();
        newFirestation.setAddress("123 Main St");
        newFirestation.setStation(1);

        // Send a POST request to create the new Firestation
        ResponseEntity<Firestations> createResponse = restTemplate.postForEntity("/firestation", newFirestation, Firestations.class);

        // Assert that the POST request was successful
        assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        // Get the ID of the newly created Firestation
        long id = createResponse.getBody().getStation();

        // Update the Firestation with the new information
        newFirestation.setStation(2);
        ResponseEntity<Firestations> updateResponse = restTemplate.exchange("/firestation/{id}", HttpMethod.PUT, new HttpEntity<>(newFirestation), Firestations.class, id);

        // Assert that the PUT request was successful
        assertThat(updateResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(updateResponse.getBody().getStation()).isEqualTo(2);
    }

}