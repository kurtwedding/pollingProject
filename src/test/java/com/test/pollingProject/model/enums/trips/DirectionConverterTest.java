package com.test.pollingProject.model.enums.trips;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.junit.jupiter.api.Assertions.*;

class DirectionConverterTest {

    DirectionConverter converter = new DirectionConverter();

    @Test
    void shouldDefaultToUnknownCodeWhenConvertingNullToDatabaseColumn() {
        assertEquals(Direction.UNKNOWN.getCode(), converter.convertToDatabaseColumn(null));
    }

    @ParameterizedTest
    @EnumSource(Direction.class)
    void shouldConvertEnumToDatabaseColumn(Direction status) {
        Integer dbValue = converter.convertToDatabaseColumn(status);
        assertEquals(status.getCode(), dbValue);
    }

    @Test
    void shouldDefaultToUnknownWhenConvertingNullToEntityAttribute() {
        assertEquals(Direction.UNKNOWN, converter.convertToEntityAttribute(null));
    }

    @Test
    void shouldConvertDatabaseColumnToEntityAttribute() {
        assertEquals(Direction.INBOUND, converter.convertToEntityAttribute(1));
        assertEquals(Direction.OUTBOUND, converter.convertToEntityAttribute(0));
    }

    @Test
    void shouldConvertUnknownDatabaseColumnToDefault() {
        assertEquals(Direction.UNKNOWN, converter.convertToEntityAttribute(999));
    }
}
