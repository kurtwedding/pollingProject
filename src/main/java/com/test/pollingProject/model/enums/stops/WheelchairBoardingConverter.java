package com.test.pollingProject.model.enums.stops;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class WheelchairBoardingConverter implements AttributeConverter<WheelchairBoarding, Integer> {

    @Override
    public Integer convertToDatabaseColumn(WheelchairBoarding attribute) {
        if (attribute != null) {
            return attribute.getCode();
        }
        return null;
    }

    @Override
    public WheelchairBoarding convertToEntityAttribute(Integer dbData) {
        if (dbData != null) {
            return WheelchairBoarding.fromCode(String.valueOf(dbData));
        }
        return null;
    }

}
