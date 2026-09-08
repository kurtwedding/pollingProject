package com.test.pollingProject.model.enums.trips;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class WheelchairAccessibleConverter implements AttributeConverter<WheelchairAccessible, Integer> {

    @Override
    public Integer convertToDatabaseColumn(WheelchairAccessible attribute) {
        if (attribute != null) {
            return attribute.getCode();
        }
        return null;
    }

    @Override
    public WheelchairAccessible convertToEntityAttribute(Integer dbData) {
        if (dbData != null) {
            return WheelchairAccessible.fromCode(String.valueOf(dbData));
        }
        return null;
    }

}
