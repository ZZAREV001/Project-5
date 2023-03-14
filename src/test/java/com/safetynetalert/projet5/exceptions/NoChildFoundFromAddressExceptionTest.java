package com.safetynetalert.projet5.exceptions;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

public class NoChildFoundFromAddressExceptionTest {

    @Test
    void itShouldThrowNoChildFoundFromAddressException() {
        String address = "123 Main St";
        assertThatThrownBy(() -> {
            throw new NoChildFoundFromAddressException(address);
        })
                .isInstanceOf(NoChildFoundFromAddressException.class)
                .hasMessage("No child(ren) found for address : " + address + " !")
                .hasFieldOrPropertyWithValue("address", address);
    }
}
