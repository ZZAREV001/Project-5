package com.safetynetalert.projet5.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;


public class PersonTest {

    @Test
    public void testPersonConstructor() {
        Person person = new Person("John", "Doe", "123 Main St", "Anytown", "12345", "555-555-5555", "jdoe@example.com");
        assertEquals("John", person.getFirstName());
        assertEquals("Doe", person.getLastName());
        assertEquals("123 Main St", person.getAddress());
        assertEquals("Anytown", person.getCity());
        assertEquals("12345", person.getZip());
        assertEquals("555-555-5555", person.getPhone());
        assertEquals("jdoe@example.com", person.getEmail());
    }

    @Test
    public void testGettersAndSetters() {
        Person person = new Person();
        person.setFirstName("John");
        assertEquals("John", person.getFirstName());

        person.setLastName("Doe");
        assertEquals("Doe", person.getLastName());

        person.setAddress("123 Main St");
        assertEquals("123 Main St", person.getAddress());

        person.setCity("Anytown");
        assertEquals("Anytown", person.getCity());

        person.setZip("12345");
        assertEquals("12345", person.getZip());

        person.setPhone("555-555-5555");
        assertEquals("555-555-5555", person.getPhone());

        person.setEmail("jdoe@example.com");
        assertEquals("jdoe@example.com", person.getEmail());
    }

    @Test
    public void testEquals() {
        // Setup
        final Object other = null;
        final Person person = new Person();

        // Run the test
        final boolean result = person.equals(other);

        // Verify the results
        assertFalse(result);
    }


}
