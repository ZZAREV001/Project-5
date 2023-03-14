package com.safetynetalert.projet5.exceptions;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class NoFirestationFoundExceptionTest {

    @Test
    void itShouldGetFirestationNb() {
        // arrange
        List<Integer> firestationNb = Arrays.asList(1, 2, 3);
        NoFirestationFoundException ex = new NoFirestationFoundException(firestationNb);

        // act
        List<Integer> result = ex.getFirestationNb();

        // assert
        assertThat(result).isNotNull();
    }
}
