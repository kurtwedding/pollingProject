package com.test.pollingProject.model.enums.trips;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class WheelchairAccessibleTest {

    @ParameterizedTest
    @CsvSource({
            "0, UNKNOWN",
            "1, ACCESSIBLE",
            "2, INACCESSIBLE"
    })
    void shouldParseValidCodes(String code, WheelchairAccessible status) {
        assertEquals(WheelchairAccessible.fromCode(code), status);
    }

    @ParameterizedTest
    @ValueSource(strings = { "99", "abc", "", "unknown" })
    void shouldReturnUnknownForInvalidCodes(String code) {
        assertEquals(WheelchairAccessible.fromCode(code), WheelchairAccessible.UNKNOWN);
    }

    @Test
    void shouldHandleNullGracefully() {
        assertEquals(WheelchairAccessible.fromCode(null), WheelchairAccessible.UNKNOWN);
    }

    @ParameterizedTest
    @CsvSource({
            "No information available, UNKNOWN",
            "accessible, ACCESSIBLE",
            "INACCESSIBLE, INACCESSIBLE"
    })
    void shouldParseValidLabels(String label, WheelchairAccessible status) {
        assertEquals(WheelchairAccessible.fromLabel(label), status);
    }

    @Test
    void shouldThrowExceptionForUnknownLabel() {
        assertThrows(IllegalArgumentException.class, () -> WheelchairAccessible.fromLabel("Invalid Label"));
    }
}
