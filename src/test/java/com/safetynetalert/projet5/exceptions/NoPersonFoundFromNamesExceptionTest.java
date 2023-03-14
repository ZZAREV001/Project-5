package com.safetynetalert.projet5.exceptions;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class NoPersonFoundFromNamesExceptionTest {

    @Test
    void testNoPersonFoundFromNamesException() {
        String firstName = "John";
        String lastName = "Doe";
        NoPersonFoundFromNamesException exception = assertThrows(
                NoPersonFoundFromNamesException.class,
                () -> { throw new NoPersonFoundFromNamesException(firstName, lastName); }
        );
        assertThat(exception)
                .hasMessage("No persons found for this firstname and lastname : " + firstName + " ," + lastName + " !")
                .hasFieldOrPropertyWithValue("firstName", firstName)
                .hasFieldOrPropertyWithValue("lastName", lastName);
    }
}
