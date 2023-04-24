package com.safetynetalert.projet5.service.impl;

import com.safetynetalert.projet5.model.MedicalRecords;
import com.safetynetalert.projet5.model.Person;
import com.safetynetalert.projet5.repository.DataFileAccess;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MedicalRecordsServiceImplTest {

    @Mock
    private DataFileAccess dataFileAccess;

    @InjectMocks
    private MedicalRecordsServiceImpl medicalRecordsService;

    @Test
    void iTShouldGetMedicationsFromPerson() {
        // GIVEN
        Person person = new Person("John", "Doe", "address", "city", "12345", "phone", "email");
        MedicalRecords medicalRecords = new MedicalRecords("John", "Doe", "birthdate", Arrays.asList("med1", "med2"), Collections.emptyList());

        // WHEN
        when(dataFileAccess.getMedicalrecords()).thenReturn(Collections.singletonList(medicalRecords));

        List<String> medications = medicalRecordsService.getMedicationsFromPerson(person);

        // THEN
        assertEquals(2, medications.size());
        assertEquals("med1", medications.get(0));
        assertEquals("med2", medications.get(1));
    }

    @Test
    void getMedicationsFromPerson_notFound() {
        Person person = new Person("Jane", "Doe", "address", "city", "12345", "phone", "email");

        when(dataFileAccess.getMedicalrecords()).thenReturn(Collections.emptyList());

        List<String> medications = medicalRecordsService.getMedicationsFromPerson(person);

        assertNull(medications);
    }

    @Test
    void iTShouldGetAllergiesFromPerson() {
        // Given
        Person person = new Person("John", "Doe", "address", "city", "12345", "phone", "email");
        MedicalRecords medicalRecords = new MedicalRecords("John", "Doe", "birthdate", Collections.emptyList(), Arrays.asList("allergy1", "allergy2"));

        // When
        when(dataFileAccess.getMedicalrecords()).thenReturn(Collections.singletonList(medicalRecords));

        List<String> allergies = medicalRecordsService.getAllergiesFromPerson(person);
        // Then
        assertEquals(2, allergies.size());
        assertEquals("allergy1", allergies.get(0));
        assertEquals("allergy2", allergies.get(1));
    }

    @Test
    void getAllergiesFromPerson_notFound() {
        Person person = new Person("Jane", "Doe", "address", "city", "12345", "phone", "email");

        when(dataFileAccess.getMedicalrecords()).thenReturn(Collections.emptyList());

        List<String> allergies = medicalRecordsService.getAllergiesFromPerson(person);

        assertNull(allergies);
    }

}