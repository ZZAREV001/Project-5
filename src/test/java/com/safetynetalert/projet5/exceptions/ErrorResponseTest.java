package com.safetynetalert.projet5.exceptions;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ErrorResponseTest {

    @Test
    void testErrorResponseConstructor() {
        ErrorResponse errorResponse = new ErrorResponse(404, "NOT_FOUND", "The requested resource was not found");
        assertThat(errorResponse.getStatus()).isEqualTo(404);
        assertThat(errorResponse.getErrorCode()).isEqualTo("NOT_FOUND");
        assertThat(errorResponse.getMessage()).isEqualTo("The requested resource was not found");
    }

}
