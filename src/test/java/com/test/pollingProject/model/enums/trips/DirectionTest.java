package com.test.pollingProject.model.enums.trips;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class DirectionTest {

    @ParameterizedTest
    @CsvSource({
            "0, OUTBOUND",
            "1, INBOUND",
            "9999, UNKNOWN"
    })
    void shouldParseValidCodes(String code, Direction status) {
        assertEquals(Direction.fromCode(code), status);
    }

    @ParameterizedTest
    @ValueSource(strings = { "99", "abc", "", "unknown" })
    void shouldReturnUnknownForInvalidCodes(String code) {
        assertEquals(Direction.fromCode(code), Direction.UNKNOWN);
    }

    @Test
    void shouldHandleNullGracefully() {
        assertEquals(Direction.fromCode(null), Direction.UNKNOWN);
    }

    @ParameterizedTest
    @CsvSource({
            "Outbound, OUTBOUND",
            "inbound, INBOUND",
            "UNKNOWN, UNKNOWN"
    })
    void shouldParseValidLabels(String label, Direction status) {
        assertEquals(Direction.fromLabel(label), status);
    }

    @Test
    void shouldThrowExceptionForUnknownLabel() {
        assertThrows(IllegalArgumentException.class, () -> Direction.fromLabel("Invalid Label"));
    }
}
