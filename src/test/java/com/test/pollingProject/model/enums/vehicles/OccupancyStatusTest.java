package com.test.pollingProject.model.enums.vehicles;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class OccupancyStatusTest {

    @ParameterizedTest
    @CsvSource({
            "0, EMPTY",
            "1, MANY_SEATS_AVAILABLE",
            "2, FEW_SEATS_AVAILABLE",
            "3, STANDING_ROOM_ONLY",
            "4, CRUSHED_STANDING_ROOM_ONLY",
            "5, FULL",
            "6, NOT_ACCEPTING_PASSENGERS",
            "7, NO_DATA_AVAILABLE",
            "8, NOT_BOARDABLE"
    })
    void shouldParseValidCodes(String code, OccupancyStatus status) {
        assertEquals(OccupancyStatus.fromCode(code), status);
    }

    @ParameterizedTest
    @ValueSource(strings = { "99", "abc", "", "unknown" })
    void shouldReturnNoDataAvailableForInvalidCodes(String code) {
        assertEquals(OccupancyStatus.fromCode(code), OccupancyStatus.NO_DATA_AVAILABLE);
    }

    @Test
    void shouldHandleNullGracefully() {
        assertEquals(OccupancyStatus.fromCode(null), OccupancyStatus.NO_DATA_AVAILABLE);
    }

    @ParameterizedTest
    @CsvSource({
            "Vacant, EMPTY",
            "lots of space, MANY_SEATS_AVAILABLE",
            "FULL, FULL"
    })
    void shouldParseValidLabels(String label, OccupancyStatus status) {
        assertEquals(OccupancyStatus.fromLabel(label), status);
    }

    @Test
    void shouldThrowExceptionForUnknownLabel() {
        assertThrows(IllegalArgumentException.class, () -> OccupancyStatus.fromLabel("Invalid Label"));
    }

}
