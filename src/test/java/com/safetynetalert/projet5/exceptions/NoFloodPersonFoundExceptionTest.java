package com.safetynetalert.projet5.exceptions;

import org.assertj.core.api.AbstractThrowableAssert;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.hamcrest.MatcherAssert.assertThat;

public class NoFloodPersonFoundExceptionTest {

    @Test
    public void itShouldThrowNoFloodPersonFoundException() {
        List<Integer> stationNumbers = Arrays.asList(1, 2, 3);
        int expectedStationNumber = 4;

        AbstractThrowableAssert<?, ? extends Throwable> exception =
                assertThatThrownBy(() -> {
                    throw new NoFloodPersonFoundException(stationNumbers, expectedStationNumber);
                })
                        .isInstanceOf(NoFloodPersonFoundException.class)
                        .hasMessageContaining("No station number(s) found for number");
    }
}
