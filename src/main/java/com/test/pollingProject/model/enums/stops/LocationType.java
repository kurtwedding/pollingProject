package com.test.pollingProject.model.enums.stops;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

/**
 * location_type is returned by /gtfs/stops endpoint
 * LocationType
 */
@Getter
public enum LocationType {
    STOP(0, "Stop"),
    STATION(1, "Station"),
    ENTRANCE_EXIT(2, "Entrance/Exit"),
    GENERIC(3, "Generic Node"),
    BOARDING_AREA(4, "Boarding Area"),

    UNKNOWN(9999, "UNKNOWN"); // Hopefully there should never be an unknown, but just incase

    private final int code;
    private final String label;

    LocationType(int code, String label) {
        this.code = code;
        this.label = label;
    }

    @JsonCreator
    public static LocationType fromCode(String code) {
        int intCode;
        try {
            intCode = Integer.parseInt(code);
        } catch (NumberFormatException e) {
            return UNKNOWN; // If the code is not a valid integer, return UNKNOWN
        }
        for (LocationType type : values()) {
            if (intCode == type.getCode())
                return type;
        }
        // If the value is not one of the known above
        System.out.println("Unknown location type code: " + intCode);
        return UNKNOWN;
    }

    public static LocationType fromLabel(String label) {
        for (LocationType type : values()) {
            if (type.label.equalsIgnoreCase(label)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown location type label: " + label);
    }

}
