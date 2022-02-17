package com.safetynetalert.projet5.controller;

import com.safetynetalert.projet5.model.DataFile;
import com.safetynetalert.projet5.repository.DataFileAccess;
import com.safetynetalert.projet5.service.FireStationsService;
import com.safetynetalert.projet5.service.impl.FireStationsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebMvcTest(DataController.class)
class DataControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FireStationsServiceImpl fireStationsServiceImpl;

    @MockBean
    private DataController underTest;

    @BeforeEach
    void setup() throws Exception {
        MockitoAnnotations.openMocks(this);
        underTest = new DataController();
    }

    @Test
    void iTShouldGetFireStationsByID() {
        // Given

        // When
        // Then
    }

    @Test
    void iTShouldGetPhoneAlert() {
        // Given
        // When
        // Then
    }

    @Test
    void iTShouldGetCommunityEmail() {
        // Given
        // When
        // Then
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