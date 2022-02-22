package com.safetynetalert.projet5.service.impl;

import com.safetynetalert.projet5.model.Person;
import com.safetynetalert.projet5.repository.DataFileAccess;
import com.safetynetalert.projet5.service.PersonService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
@NoArgsConstructor
public class PersonServiceImpl implements PersonService {

    @Autowired
    DataFileAccess dataFileAccess;


    @Override
    public Person savePerson(Person model) {
        Person result = dataFileAccess.savePerson(model);
        if (result != null) log.info("Request save person successful!");
        log.info("Request save person failed.");
        return result;
    }
}
