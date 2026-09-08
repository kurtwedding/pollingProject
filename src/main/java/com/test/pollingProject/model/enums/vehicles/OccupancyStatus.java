package com.test.pollingProject.model.enums.vehicles;

import com.fasterxml.jackson.annotation.JsonCreator;

import lombok.Getter;

@Getter
public enum OccupancyStatus {
    EMPTY(0, "Vacant"),
    MANY_SEATS_AVAILABLE(1, "Lots of space"),
    FEW_SEATS_AVAILABLE(2, "Moderate"),
    STANDING_ROOM_ONLY(3, "Standing only"),
    CRUSHED_STANDING_ROOM_ONLY(4, "Crowded"),
    FULL(5, "Full"),
    NOT_ACCEPTING_PASSENGERS(6, "Not accepting passengers"),
    NO_DATA_AVAILABLE(7, "Unknown"),
    NOT_BOARDABLE(8, "Non-boardable");

    private final int code;
    private final String label;

    OccupancyStatus(int code, String label) {
        this.code = code;
        this.label = label;
    }

    @JsonCreator
    public static OccupancyStatus fromCode(String code) {
        int intCode;
        try {
            intCode = Integer.parseInt(code);
        } catch (NumberFormatException e) {
            return NO_DATA_AVAILABLE; // If the code is not a valid integer, return UNKNOWN
        }
        for (OccupancyStatus type : values()) {
            if (intCode == type.getCode())
                return type;
        }
        // If the value is not one of the known above
        System.out.println("UNKNOWN LOCATION TYPE: " + intCode);
        return NO_DATA_AVAILABLE;
    }
}
