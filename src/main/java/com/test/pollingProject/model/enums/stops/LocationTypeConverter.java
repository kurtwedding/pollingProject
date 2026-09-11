package com.test.pollingProject.model.enums.stops;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class LocationTypeConverter implements AttributeConverter<LocationType, Integer> {

    @Override
    public Integer convertToDatabaseColumn(LocationType attribute) {
        if (attribute != null) {
            return attribute.getCode();
        }
        return LocationType.UNKNOWN.getCode(); // Defaults to nothing
    }

    @Override
    public LocationType convertToEntityAttribute(Integer dbData) {
        if (dbData != null) {
            return LocationType.fromCode(String.valueOf(dbData));
        }
        return LocationType.UNKNOWN;
    }
}
