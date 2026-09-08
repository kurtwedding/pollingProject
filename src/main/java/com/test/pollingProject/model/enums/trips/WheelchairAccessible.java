package com.test.pollingProject.model.enums.trips;

import com.fasterxml.jackson.annotation.JsonCreator;

import lombok.Getter;

@Getter
public enum WheelchairAccessible {
    UNKNOWN(0, "No information available"),
    ACCESSIBLE(1, "Accessible"),
    INACCESSIBLE(2, "Inaccessible");

    private final int code;
    private final String label;

    WheelchairAccessible(int code, String label) {
        this.code = code;
        this.label = label;
    }

    @JsonCreator
    public static WheelchairAccessible fromCode(String code) {
        int intCode;
        try {
            intCode = Integer.parseInt(code);
        } catch (NumberFormatException e) {
            return UNKNOWN; // If the code is not a valid integer, return UNKNOWN
        }
        for (WheelchairAccessible type : values()) {
            if (intCode == type.getCode())
                return type;
        }
        // If the value is not one of the known above
        System.out.println("UNKNOWN WHEELCHAIR ACCESS: " + intCode);
        return UNKNOWN;
    }

}
