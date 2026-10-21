package com.test.pollingProject.model.enums.stops;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class WheelchairBoardingTest {

    @ParameterizedTest
    @CsvSource({
            "0, UNKNOWN",
            "1, ACCESSIBLE",
            "2, INACCESSIBLE"
    })
    void shouldParseValidCodes(String code, WheelchairBoarding status) {
        assertEquals(WheelchairBoarding.fromCode(code), status);
    }

    @ParameterizedTest
    @ValueSource(strings = { "99", "abc", "", "unknown" })
    void shouldReturnUnknownForInvalidCodes(String code) {
        assertEquals(WheelchairBoarding.fromCode(code), WheelchairBoarding.UNKNOWN);
    }

    @Test
    void shouldHandleNullGracefully() {
        assertEquals(WheelchairBoarding.fromCode(null), WheelchairBoarding.UNKNOWN);
    }

    @ParameterizedTest
    @CsvSource({
            "Unknown, UNKNOWN",
            "wheelchair accessible, ACCESSIBLE",
            "NOT WHEELCHAIR ACCESSIBLE, INACCESSIBLE"
    })
    void shouldParseValidLabels(String label, WheelchairBoarding status) {
        assertEquals(WheelchairBoarding.fromLabel(label), status);
    }

    @Test
    void shouldThrowExceptionForUnknownLabel() {
        assertThrows(IllegalArgumentException.class, () -> WheelchairBoarding.fromLabel("Invalid Label"));
    }
}
