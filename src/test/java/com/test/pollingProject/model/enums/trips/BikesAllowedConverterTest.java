package com.test.pollingProject.model.enums.trips;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.junit.jupiter.api.Assertions.*;

class BikesAllowedConverterTest {

    BikesAllowedConverter converter = new BikesAllowedConverter();

    @Test
    void shouldDefaultToUnknownCodeWhenConvertingNullToDatabaseColumn() {
        assertEquals(BikesAllowed.UNKNOWN.getCode(), converter.convertToDatabaseColumn(null));
    }

    @ParameterizedTest
    @EnumSource(BikesAllowed.class)
    void shouldConvertEnumToDatabaseColumn(BikesAllowed status) {
        Integer dbValue = converter.convertToDatabaseColumn(status);
        assertEquals(status.getCode(), dbValue);
    }

    @Test
    void shouldDefaultToUnknownWhenConvertingNullToEntityAttribute() {
        assertEquals(BikesAllowed.UNKNOWN, converter.convertToEntityAttribute(null));
    }

    @Test
    void shouldConvertDatabaseColumnToEntityAttribute() {
        assertEquals(BikesAllowed.ALLOWED, converter.convertToEntityAttribute(1));
        assertEquals(BikesAllowed.NOT_ALLOWED, converter.convertToEntityAttribute(2));
    }

    @Test
    void shouldConvertUnknownDatabaseColumnToDefault() {
        assertEquals(BikesAllowed.UNKNOWN, converter.convertToEntityAttribute(999));
    }
}
