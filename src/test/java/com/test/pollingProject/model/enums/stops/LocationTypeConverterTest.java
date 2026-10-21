package com.test.pollingProject.model.enums.stops;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.junit.jupiter.api.Assertions.*;

class LocationTypeConverterTest {

    LocationTypeConverter converter = new LocationTypeConverter();

    @Test
    void shouldDefaultToUnknownCodeWhenConvertingNullToDatabaseColumn() {
        assertEquals(LocationType.UNKNOWN.getCode(), converter.convertToDatabaseColumn(null));
    }

    @ParameterizedTest
    @EnumSource(LocationType.class)
    void shouldConvertEnumToDatabaseColumn(LocationType status) {
        Integer dbValue = converter.convertToDatabaseColumn(status);
        assertEquals(status.getCode(), dbValue);
    }

    @Test
    void shouldDefaultToUnknownWhenConvertingNullToEntityAttribute() {
        assertEquals(LocationType.UNKNOWN, converter.convertToEntityAttribute(null));
    }

    @Test
    void shouldConvertDatabaseColumnToEntityAttribute() {
        assertEquals(LocationType.STOP, converter.convertToEntityAttribute(0));
        assertEquals(LocationType.STATION, converter.convertToEntityAttribute(1));
        assertEquals(LocationType.BOARDING_AREA, converter.convertToEntityAttribute(4));
    }

    @Test
    void shouldConvertUnknownDatabaseColumnToDefault() {
        assertEquals(LocationType.UNKNOWN, converter.convertToEntityAttribute(999));
    }
}
