package com.test.pollingProject.model.enums.stops;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.junit.jupiter.api.Assertions.*;

class WheelchairBoardingConverterTest {

    WheelchairBoardingConverter converter = new WheelchairBoardingConverter();

    @Test
    void shouldDefaultToUnknownCodeWhenConvertingNullToDatabaseColumn() {
        assertEquals(WheelchairBoarding.UNKNOWN.getCode(), converter.convertToDatabaseColumn(null));
    }

    @ParameterizedTest
    @EnumSource(WheelchairBoarding.class)
    void shouldConvertEnumToDatabaseColumn(WheelchairBoarding status) {
        Integer dbValue = converter.convertToDatabaseColumn(status);
        assertEquals(status.getCode(), dbValue);
    }

    @Test
    void shouldDefaultToUnknownWhenConvertingNullToEntityAttribute() {
        assertEquals(WheelchairBoarding.UNKNOWN, converter.convertToEntityAttribute(null));
    }

    @Test
    void shouldConvertDatabaseColumnToEntityAttribute() {
        assertEquals(WheelchairBoarding.ACCESSIBLE, converter.convertToEntityAttribute(1));
        assertEquals(WheelchairBoarding.INACCESSIBLE, converter.convertToEntityAttribute(2));
    }

    @Test
    void shouldConvertUnknownDatabaseColumnToDefault() {
        assertEquals(WheelchairBoarding.UNKNOWN, converter.convertToEntityAttribute(999));
    }
}
