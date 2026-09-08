package com.test.pollingProject.model.enums.vehicles;

import jakarta.persistence.AttributeConverter;

public class OccupancyStatusConverter implements AttributeConverter<OccupancyStatus, Integer> {

    @Override
    public Integer convertToDatabaseColumn(OccupancyStatus attribute) {
        if (attribute == null)
            return null;
        return attribute.getCode();
    }

    @Override
    public OccupancyStatus convertToEntityAttribute(Integer dbData) {
        if (dbData == null)
            return null;
        return OccupancyStatus.fromCode(String.valueOf(dbData));
    }

}
