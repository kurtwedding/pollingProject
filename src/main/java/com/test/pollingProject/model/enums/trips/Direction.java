package com.test.pollingProject.model.enums.trips;

import com.fasterxml.jackson.annotation.JsonCreator;

import lombok.Getter;

@Getter
public enum Direction {
    OUTBOUND(0, "Outbound"),
    INBOUND(1, "Inbound"),
    UNKNOWN(9999, "Unknown");

    private final int code;
    private final String label;

    Direction(int code, String label) {
        this.code = code;
        this.label = label;
    }

    @JsonCreator
    public static Direction fromCode(String code) {
        int intCode;
        try {
            intCode = Integer.parseInt(code);
        } catch (NumberFormatException e) {
            return UNKNOWN; // If the code is not a valid integer, return UNKNOWN
        }
        for (Direction type : values()) {
            if (intCode == type.getCode())
                return type;
        }
        // If the value is not one of the known above
        System.out.println("UNKNOWN DIRECTION: " + intCode);
        return UNKNOWN;
    }
}