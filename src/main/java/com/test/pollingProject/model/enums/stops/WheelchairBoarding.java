package com.test.pollingProject.model.enums.stops;

import com.fasterxml.jackson.annotation.JsonCreator;

import lombok.Getter;

@Getter
public enum WheelchairBoarding {
    UNKNOWN(0, "Unknown"),
    ACCESSIBLE(1, "Wheelchair Accessible"),
    INACCESSIBLE(2, "Not Wheelchair Accessible");

    private final int code;
    private final String label;

    WheelchairBoarding(int code, String label) {
        this.code = code;
        this.label = label;
    }

    @JsonCreator
    public static WheelchairBoarding fromCode(String code) {
        int intCode;
        try {
            intCode = Integer.parseInt(code);
        } catch (NumberFormatException e) {
            return UNKNOWN; // If the code is not a valid integer, return UNKNOWN
        }
        for (WheelchairBoarding type : values()) {
            if (intCode == type.getCode())
                return type;
        }
        // If the value is not one of the known above
        System.out.println("Unknown wheelchair boarding code: " + intCode);
        return UNKNOWN;
    }

    public static WheelchairBoarding fromLabel(String label) {
        for (WheelchairBoarding type : values()) {
            if (type.label.equalsIgnoreCase(label)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown wheelchair boarding label: " + label);
    }
}
