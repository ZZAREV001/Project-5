package com.safetynetalert.projet5.service.impl;

import com.safetynetalert.projet5.exceptions.ControllerAdvisor;
import com.safetynetalert.projet5.model.MedicalRecords;
import com.safetynetalert.projet5.model.Person;
import com.safetynetalert.projet5.repository.DataFileAccess;
import com.safetynetalert.projet5.service.MedicalRecordsService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MedicalRecordsServiceImpl implements MedicalRecordsService {

    private static final Logger log = LogManager.getLogger(ControllerAdvisor.class);

    @Autowired
    private DataFileAccess dataFileAccess;

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