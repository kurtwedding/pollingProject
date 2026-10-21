package com.test.pollingProject.model.enums.trips;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class BikesAllowedTest {

    @ParameterizedTest
    @CsvSource({
            "0, UNKNOWN",
            "1, ALLOWED",
            "2, NOT_ALLOWED"
    })
    void shouldParseValidCodes(String code, BikesAllowed status) {
        assertEquals(BikesAllowed.fromCode(code), status);
    }

    @ParameterizedTest
    @ValueSource(strings = { "99", "abc", "", "unknown" })
    void shouldReturnUnknownForInvalidCodes(String code) {
        assertEquals(BikesAllowed.fromCode(code), BikesAllowed.UNKNOWN);
    }

    @Test
    void shouldHandleNullGracefully() {
        assertEquals(BikesAllowed.fromCode(null), BikesAllowed.UNKNOWN);
    }

    @ParameterizedTest
    @CsvSource({
            "No information available, UNKNOWN",
            "bikes allowed, ALLOWED",
            "BIKES NOT ALLOWED, NOT_ALLOWED"
    })
    void shouldParseValidLabels(String label, BikesAllowed status) {
        assertEquals(BikesAllowed.fromLabel(label), status);
    }

    @Test
    void shouldThrowExceptionForUnknownLabel() {
        assertThrows(IllegalArgumentException.class, () -> BikesAllowed.fromLabel("Invalid Label"));
    }
}
