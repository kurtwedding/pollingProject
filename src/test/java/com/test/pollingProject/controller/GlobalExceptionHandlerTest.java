package com.test.pollingProject.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Map;

import org.junit.jupiter.api.Test;

public class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void shouldHandleIllegalArgumentAndReturnErrorAndMessage() {
        IllegalArgumentException exception = new IllegalArgumentException("Invalid parameter value");

        Map<String, String> response = handler.handleIllegalArgument(exception);

        assertEquals("Bad Request", response.get("error"));
        assertEquals("Invalid parameter value", response.get("message"));
    }
}
