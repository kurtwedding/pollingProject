package com.test.pollingProject.model.enums.routes;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class RouteTypeTest {

    @ParameterizedTest
    @CsvSource({
            "0, TRAM",
            "1, SUBWAY",
            "2, RAIL",
            "3, BUS",
            "4, FERRY",
            "5, CABLE_CAR",
            "6, LIFT",
            "7, FUNICULAR",
            "11, TROLLEYBUS",
            "12, MONORAIL",
            "9999, UNKNOWN"
    })
    void shouldParseValidCodes(String code, RouteType routeType) {
        assertEquals(RouteType.fromCode(code), routeType);
    }

    @ParameterizedTest
    @ValueSource(strings = { "99", "abc", "", "unknown" })
    void shouldReturnNoDataAvailableForInvalidCodes(String code) {
        assertEquals(RouteType.fromCode(code), RouteType.UNKNOWN);
    }

    @Test
    void shouldHandleNullGracefully() {
        assertEquals(RouteType.fromCode(null), RouteType.UNKNOWN);
    }

    @ParameterizedTest
    @CsvSource({
            "Bus, BUS",
            "cable car, CABLE_CAR",
            "MONORAIL, MONORAIL"
    })
    void shouldParseValidLabels(String label, RouteType routeType) {
        assertEquals(RouteType.fromLabel(label), routeType);
    }

    @Test
    void shouldThrowExceptionForUnknownLabel() {
        assertThrows(IllegalArgumentException.class, () -> RouteType.fromLabel("Invalid Label"));
    }
}
