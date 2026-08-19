package com.test.pollingProject.model.enums;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class RouteTypeConverter implements AttributeConverter<RouteType, Integer> {

    @Override
    public Integer convertToDatabaseColumn(RouteType attribute) {
        return attribute.getCode();
    }

    @Override
    public RouteType convertToEntityAttribute(Integer dbData) {
        return RouteType.fromCode(String.valueOf(dbData));
    }
}
