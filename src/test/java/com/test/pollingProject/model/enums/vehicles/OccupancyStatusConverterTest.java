package com.test.pollingProject.model.enums.vehicles;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.junit.jupiter.api.Assertions.*;

class OccupancyStatusConverterTest {

    private final OccupancyStatusConverter converter = new OccupancyStatusConverter();

    @Test
    void shouldDefaultToUnknownCodeWhenConvertingNullToDatabaseColumn() {
        assertEquals(OccupancyStatus.NO_DATA_AVAILABLE.getCode(), converter.convertToDatabaseColumn(null));
    }

    @ParameterizedTest
    @EnumSource(OccupancyStatus.class)
    void shouldConvertEnumToDatabaseColumn(OccupancyStatus status) {
        Integer dbValue = converter.convertToDatabaseColumn(status);
        assertEquals(status.getCode(), dbValue);
    }

    @Test
    void shouldDefaultToUnknownWhenConvertingNullToEntityAttribute() {
        assertEquals(OccupancyStatus.NO_DATA_AVAILABLE, converter.convertToEntityAttribute(null));
    }

    @Test
    void shouldConvertDatabaseColumnToEntityAttribute() {
        assertEquals(OccupancyStatus.EMPTY, converter.convertToEntityAttribute(0));
        assertEquals(OccupancyStatus.MANY_SEATS_AVAILABLE, converter.convertToEntityAttribute(1));
    }

    @Test
    void shouldConvertUnknownDatabaseColumnToDefault() {
        assertEquals(OccupancyStatus.NO_DATA_AVAILABLE, converter.convertToEntityAttribute(999));
    }
}
