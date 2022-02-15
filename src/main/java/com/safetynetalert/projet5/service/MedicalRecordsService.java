package com.safetynetalert.projet5.service;



import com.safetynetalert.projet5.model.MedicalRecords;
import com.safetynetalert.projet5.model.Person;

import java.util.List;

public interface MedicalRecordsService {

    List<String> getMedicationsFromPerson(Person person);

    List<String> getAllergiesFromPerson(Person person);

}
