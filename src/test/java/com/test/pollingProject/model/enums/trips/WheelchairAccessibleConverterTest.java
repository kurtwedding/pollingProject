package com.test.pollingProject.model.enums.trips;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.junit.jupiter.api.Assertions.*;

class WheelchairAccessibleConverterTest {

    WheelchairAccessibleConverter converter = new WheelchairAccessibleConverter();

    @Test
    void shouldDefaultToUnknownCodeWhenConvertingNullToDatabaseColumn() {
        assertEquals(WheelchairAccessible.UNKNOWN.getCode(), converter.convertToDatabaseColumn(null));
    }

    @ParameterizedTest
    @EnumSource(WheelchairAccessible.class)
    void shouldConvertEnumToDatabaseColumn(WheelchairAccessible status) {
        Integer dbValue = converter.convertToDatabaseColumn(status);
        assertEquals(status.getCode(), dbValue);
    }

    @Test
    void shouldDefaultToUnknownWhenConvertingNullToEntityAttribute() {
        assertEquals(WheelchairAccessible.UNKNOWN, converter.convertToEntityAttribute(null));
    }

    @Test
    void shouldConvertDatabaseColumnToEntityAttribute() {
        assertEquals(WheelchairAccessible.ACCESSIBLE, converter.convertToEntityAttribute(1));
        assertEquals(WheelchairAccessible.INACCESSIBLE, converter.convertToEntityAttribute(2));
    }

    @Test
    void shouldConvertUnknownDatabaseColumnToDefault() {
        assertEquals(WheelchairAccessible.UNKNOWN, converter.convertToEntityAttribute(999));
    }
}
