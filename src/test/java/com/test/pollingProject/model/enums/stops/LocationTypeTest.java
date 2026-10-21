package com.test.pollingProject.model.enums.stops;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class LocationTypeTest {

    @ParameterizedTest
    @CsvSource({
            "0, STOP",
            "1, STATION",
            "2, ENTRANCE_EXIT",
            "3, GENERIC",
            "4, BOARDING_AREA",
            "9999, UNKNOWN"
    })
    void shouldParseValidCodes(String code, LocationType status) {
        assertEquals(LocationType.fromCode(code), status);
    }

    @ParameterizedTest
    @ValueSource(strings = { "99", "abc", "", "unknown" })
    void shouldReturnUnknownForInvalidCodes(String code) {
        assertEquals(LocationType.fromCode(code), LocationType.UNKNOWN);
    }

    @Test
    void shouldHandleNullGracefully() {
        assertEquals(LocationType.fromCode(null), LocationType.UNKNOWN);
    }

    @ParameterizedTest
    @CsvSource({
            "Stop, STOP",
            "entrance/exit, ENTRANCE_EXIT",
            "GENERIC NODE, GENERIC"
    })
    void shouldParseValidLabels(String label, LocationType status) {
        assertEquals(LocationType.fromLabel(label), status);
    }

    @Test
    void shouldThrowExceptionForUnknownLabel() {
        assertThrows(IllegalArgumentException.class, () -> LocationType.fromLabel("Invalid Label"));
    }
}
