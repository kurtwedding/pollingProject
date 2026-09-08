package com.test.pollingProject.model.enums.trips;

import com.fasterxml.jackson.annotation.JsonCreator;

import lombok.Getter;

@Getter
public enum BikesAllowed {
    UNKNOWN(0, "No information available"),
    ALLOWED(1, "Bikes allowed"),
    NOT_ALLOWED(2, "Bikes not allowed");

    private final int code;
    private final String label;

    BikesAllowed(int code, String label) {
        this.code = code;
        this.label = label;
    }

    @JsonCreator
    public static BikesAllowed fromCode(String code) {
        int intCode;
        try {
            intCode = Integer.parseInt(code);
        } catch (NumberFormatException e) {
            return UNKNOWN; // If the code is not a valid integer, return UNKNOWN
        }
        for (BikesAllowed type : values()) {
            if (intCode == type.getCode())
                return type;
        }
        // If the value is not one of the known above
        System.out.println("UNKNOWN ROUTE TYPE: " + intCode);
        return UNKNOWN;
    }

}
