package com.test.pollingProject.model.enums.trips;

import jakarta.persistence.AttributeConverter;

public class DirectionConverter implements AttributeConverter<Direction, Integer> {

    @Override
    public Integer convertToDatabaseColumn(Direction attribute) {
        if (attribute != null) {
            return attribute.getCode();
        }
        return null;
    }

    @Override
    public Direction convertToEntityAttribute(Integer dbData) {
        if (dbData != null) {
            return Direction.fromCode(String.valueOf(dbData));
        }
        return null;
    }

}
