package com.test.pollingProject.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

@Getter
public enum RouteType {
    TRAM(0, "Tram"),
    SUBWAY(1, "Subway"),
    RAIL(2, "Rail"),
    BUS(3, "Bus"),
    FERRY(4, "Ferry"),
    CABLE_CAR(5, "Cable car"),
    LIFT(6, "Aerial Lift"),
    FUNICULAR(7, "Funicular"),
    TROLLEYBUS(11, "Electric Tram"),
    MONORAIL(12, "Monorail"),
    UNKNOWN(99, "Unknown");

    private final int code;
    private final String label;

    RouteType(int code, String label) {
        this.code = code;
        this.label = label;
    }

    @JsonCreator
    public static RouteType fromCode(String code) {
        int intCode;
        try {
            intCode = Integer.parseInt(code);
        } catch (NumberFormatException e) {
            return UNKNOWN; // If the code is not a valid integer, return UNKNOWN
        }
        for (RouteType type : values()) {
            if (intCode == type.getCode())
                return type;
        }
        // If the value is not one of the known above
        return UNKNOWN;
    }
}
