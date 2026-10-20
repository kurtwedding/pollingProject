package com.test.pollingProject.model.enums.vehicles;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class OccupancyStatusConverter implements AttributeConverter<OccupancyStatus, Integer> {

    @Override
    public Integer convertToDatabaseColumn(OccupancyStatus attribute) {
        if (attribute != null)
            return attribute.getCode();
        return OccupancyStatus.NO_DATA_AVAILABLE.getCode();
    }

    @Override
    public OccupancyStatus convertToEntityAttribute(Integer dbData) {
        if (dbData != null)
            return OccupancyStatus.fromCode(String.valueOf(dbData));
        return OccupancyStatus.NO_DATA_AVAILABLE;
    }

}
