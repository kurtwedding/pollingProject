package com.test.pollingProject.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withServerError;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import com.test.pollingProject.model.Route;
import com.test.pollingProject.model.Vehicle;
import com.test.pollingProject.model.enums.vehicles.OccupancyStatus;
import com.test.pollingProject.store.RouteRepository;
import com.test.pollingProject.store.VehicleRepository;

import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;

@ExtendWith(MockitoExtension.class)
class GtfsRtServiceTest {

    @Mock
    private VehicleRepository vehicleRepository;

    @Mock
    private RouteRepository routeRepository;

    @Captor
    private ArgumentCaptor<List<Vehicle>> vehicleListCaptor;

    private final ObjectMapper objectMapper = new JsonMapper();
    private MockRestServiceServer mockRestServiceServer;
    private GtfsRtService gtfsRtService;

    @BeforeEach
    void setUp() {
        RestClient.Builder builder = RestClient.builder();
        mockRestServiceServer = MockRestServiceServer.bindTo(builder).build();

        gtfsRtService = new GtfsRtService(vehicleRepository,
                objectMapper,
                builder,
                "test-api-key",
                "http://test-api.com",
                routeRepository);

    }

    @Test
    void shouldPollAndSaveVehiclesSuccessfully() {
        String json = """
                {
                    "entity": [
                        {
                            "vehicle": {
                                "position": {
                                    "latitude": -41.8574,
                                    "longitude": 171.3342,
                                    "bearing": 135.0
                                },
                                "vehicle": {
                                    "id": "TEST_BUS"
                                },
                                "trip": {
                                    "route_id": "TEST_ROUTE"
                                },
                                "occupancy_status": 1
                            }
                        }
                    ]
                }
                """;

        // Mock the HTTP endpoint
        mockRestServiceServer.expect(requestTo("http://test-api.com/gtfs-rt/vehiclepositions"))
                .andRespond(withSuccess(json, MediaType.APPLICATION_JSON));

        // Mock the route lookup
        when(routeRepository.getReferenceById("TEST_ROUTE")).thenReturn(new Route());

        gtfsRtService.pollAndSaveVehicles();

        mockRestServiceServer.verify();

        // Get saved database objects to inspect
        verify(vehicleRepository).saveAll(vehicleListCaptor.capture());
        List<Vehicle> savedVehicles = vehicleListCaptor.getValue();

        // Inspect
        assertEquals(1, savedVehicles.size());
        Vehicle vehicle = savedVehicles.get(0);
        assertEquals("TEST_BUS", vehicle.getId());
        assertEquals(-41.8574, vehicle.getLatitude());
        assertEquals(171.3342, vehicle.getLongitude());
        assertEquals(135.0, vehicle.getBearing());
        assertEquals(OccupancyStatus.MANY_SEATS_AVAILABLE, vehicle.getOccupancyStatus());
    }

    @Test
    void shouldPollAndSaveVehiclesThenThrowAndCatchException() {
        mockRestServiceServer.expect(requestTo("http://test-api.com/gtfs-rt/vehiclepositions"))
                .andRespond(withServerError());

        assertThrows(RestClientException.class, () -> gtfsRtService.pollAndSaveVehicles()); // assert thrown exception

        mockRestServiceServer.verify(); // verify the server received the request

        verify(vehicleRepository, never()).saveAll(any()); // verify nothing was saved
    }
}
