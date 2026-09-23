package com.test.pollingProject.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.test.pollingProject.service.GtfsRtService;
import com.test.pollingProject.store.VehicleRepository;

import tools.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
class GtfsRtServiceTest {

    @Mock
    private VehicleRepository vehicleRepository;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private GtfsRtService gtfsRtService;

    @Test
    void testServiceInitialization() {
        assertNotNull(gtfsRtService);
    }
}
