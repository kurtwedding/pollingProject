package com.test.pollingProject.model.enums.routes;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.junit.jupiter.api.Assertions.*;

class RouteTypeConverterTest {

    private final RouteTypeConverter converter = new RouteTypeConverter();

    @Test
    void shouldDefaultToUnknownCodeWhenConvertingNullToDatabaseColumn() {
        assertEquals(RouteType.UNKNOWN.getCode(), converter.convertToDatabaseColumn(null));
    }

    @ParameterizedTest
    @EnumSource(RouteType.class)
    void shouldConvertEnumToDatabaseColumn(RouteType routeType) {
        Integer dbValue = converter.convertToDatabaseColumn(routeType);
        assertEquals(routeType.getCode(), dbValue);
    }

    @Test
    void shouldDefaultToUnknownWhenConvertingNullToEntityAttribute() {
        assertEquals(RouteType.UNKNOWN, converter.convertToEntityAttribute(null));
    }

    @Test
    void shouldConvertDatabaseColumnToEntityAttribute() {
        assertEquals(RouteType.TRAM, converter.convertToEntityAttribute(0));
        assertEquals(RouteType.BUS, converter.convertToEntityAttribute(3));
    }

    @Test
    void shouldConvertUnknownDatabaseColumnToDefault() {
        assertEquals(RouteType.UNKNOWN, converter.convertToEntityAttribute(8888));
    }
}
