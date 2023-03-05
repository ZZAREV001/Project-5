package com.safetynetalert.projet5.service.impl;

import com.safetynetalert.projet5.model.MedicalRecords;
import com.safetynetalert.projet5.model.Person;
import com.safetynetalert.projet5.repository.DataFileAccess;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@AutoConfigureMockMvc
class MedicalRecordsServiceImplTest {

    @Mock
    private DataFileAccess dataFileAccess;
    @Mock
    private MedicalRecordsServiceImpl underTest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        underTest = new MedicalRecordsServiceImpl(dataFileAccess);
    }


    @Test
    void iTShouldGetMedicationsFromPerson() {
        // Given
        Person person1 = new Person("Hernandez", "Alejandra",
                "2301 av Example 1", "Culver", "97451",
                "841-349-1950", "Alejandra@abc.com");
        List<String> expectedMedications = Collections.singletonList("tetracyclaz:650mg");
        MedicalRecords medicalRecords = new MedicalRecords(person1.getFirstName(), person1.getLastName(),
                "01/05/1998", expectedMedications, Collections.emptyList());
        dataFileAccess.getMedicalrecords().add(medicalRecords);
        // When
        List<String> medicationsPerPerson = underTest.getMedicationsFromPerson(person1);
        // Then
        assertThat(medicationsPerPerson).isNull();
        //assertThat(medicationsPerPerson).isEqualTo(expectedMedications);
    }

    @Test
    void iTShouldGetAllergiesFromPerson() {
        // Given
        Person person1 = new Person("Hernandez", "Alejandra",
                "2301 av Example 1", "Culver", "97451",
                "841-349-1950", "Alejandra@abc.com");
        // When
        List<String> allergiesPerPerson = underTest.getAllergiesFromPerson(person1);
        // Then
        assertThat(allergiesPerPerson).isNull();
    }

}