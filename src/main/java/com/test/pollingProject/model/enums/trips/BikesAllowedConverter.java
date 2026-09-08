package com.test.pollingProject.model.enums.trips;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class BikesAllowedConverter implements AttributeConverter<BikesAllowed, Integer> {

    @Override
    public Integer convertToDatabaseColumn(BikesAllowed attribute) {
        if (attribute != null) {
            return attribute.getCode();
        }
        return null;
    }

    @Override
    public BikesAllowed convertToEntityAttribute(Integer dbData) {
        if (dbData != null) {
            return BikesAllowed.fromCode(String.valueOf(dbData));
        }
        return null;
    }

}
