package com.safetynetalert.projet5.service.impl;

import com.safetynetalert.projet5.model.MedicalRecords;
import com.safetynetalert.projet5.model.Person;
import com.safetynetalert.projet5.repository.DataFileAccess;
import com.safetynetalert.projet5.service.MedicalRecordsService;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@NoArgsConstructor
public class MedicalRecordsServiceImpl implements MedicalRecordsService {

    private DataFileAccess dataFileAccess;

    @Autowired
    public MedicalRecordsServiceImpl(DataFileAccess dataFileAccess) {
        this.dataFileAccess = dataFileAccess;
    }

    @Override
    public List<String> getMedicationsFromPerson(Person person) {
        for (MedicalRecords medicalRecords : dataFileAccess.getMedicalrecords()) {
            if (person.getFirstName().equals(medicalRecords.getFirstName()) &&
                    person.getLastName().equals(medicalRecords.getLastName())) {
                return medicalRecords.getMedications();
            }
        }
        return null;
    }

    @Override
    public List<String> getAllergiesFromPerson(Person person) {
        for (MedicalRecords medicalRecords : dataFileAccess.getMedicalrecords()) {
            if (person.getFirstName().equals(medicalRecords.getFirstName()) &&
                    person.getLastName().equals(medicalRecords.getLastName())) {
                return medicalRecords.getAllergies();
            }
        }
        return null;
    }



}
