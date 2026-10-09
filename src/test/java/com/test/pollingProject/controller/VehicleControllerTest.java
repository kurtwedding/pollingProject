package com.test.pollingProject.controller;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import com.test.pollingProject.model.Route;
import com.test.pollingProject.model.Vehicle;
import com.test.pollingProject.model.enums.vehicles.OccupancyStatus;
import com.test.pollingProject.store.VehicleRepository;

@WebMvcTest(VehicleController.class)
@AutoConfigureMockMvc(addFilters = false)
public class VehicleControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private VehicleRepository vehicleRepository;

    private Vehicle mockVehicle1;
    private Vehicle mockVehicle2;

    private List<Vehicle> mockVehicles;

    @BeforeEach
    void setUp() {
        mockVehicle1 = new Vehicle();
        mockVehicle1.setId("1");
        mockVehicle1.setLatitude(-40.5);
        mockVehicle1.setLongitude(171.5);
        mockVehicle1.setOccupancyStatus(OccupancyStatus.EMPTY);
        Route mockRoute1 = new Route();
        mockRoute1.setRouteId("1");
        mockVehicle1.setRoute(mockRoute1);

        mockVehicle2 = new Vehicle();
        mockVehicle2.setId("2");
        mockVehicle2.setLatitude(40.5);
        mockVehicle2.setLongitude(-171.5);
        mockVehicle2.setOccupancyStatus(OccupancyStatus.FULL);
        Route mockRoute2 = new Route();
        mockRoute2.setRouteId("2");
        mockVehicle2.setRoute(mockRoute2);

        mockVehicles = Arrays.asList(mockVehicle1, mockVehicle2);
    }

    @SuppressWarnings("unchecked")
    @Test
    void shouldReturnAllVehiclesWhenNoParams() throws Exception {

        Mockito.when(vehicleRepository.findAll(any(Specification.class)))
                .thenReturn(mockVehicles);
        mockMvc.perform(get("/api/vehicles")
                .contentType(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(mockVehicles.size()))
                .andExpect(jsonPath("$[0].id").value("1"));
    }

    @SuppressWarnings("unchecked")
    @Test
    void shouldFilterVehiclesByOccupancyStatus() throws Exception {

        Mockito.when(vehicleRepository.findAll(any(Specification.class)))
                .thenReturn(Arrays.asList(mockVehicle1));

        mockMvc.perform(get("/api/vehicles")
                .param("occupancy", "empty")
                .contentType(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].occupancy_status").value("EMPTY"));
    }

    @SuppressWarnings("unchecked")
    @Test
    void shouldFilterVehiclesByLatLonBounds() throws Exception {

        Mockito.when(vehicleRepository.findAll(any(Specification.class)))
                .thenReturn(Arrays.asList(mockVehicle1));

        mockMvc.perform(get("/api/vehicles")
                .param("minLat", "-41")
                .param("maxLat", "-40")
                .param("minLon", "171")
                .param("maxLon", "172")
                .contentType(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].latitude").value("-40.5"))
                .andExpect(jsonPath("$[0].longitude").value("171.5"));

    }

    @SuppressWarnings("unchecked")
    @Test
    void shouldFilterVehiclesByRouteId() throws Exception {

        Mockito.when(vehicleRepository.findAll(any(Specification.class)))
                .thenReturn(Arrays.asList(mockVehicle1));

        mockMvc.perform(get("/api/vehicles")
                .param("routeId", "1")
                .contentType(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].route.route_id").value("1"));

    }

    @SuppressWarnings("unchecked")
    @Test
    void shouldIgnoreFilterIfLessThanFourSpatialParamsProvided() throws Exception {

        // This test covers all 4 branches, if only one parameter is provided

        Mockito.when(vehicleRepository.findAll(any(Specification.class)))
                .thenReturn(mockVehicles);

        mockMvc.perform(get("/api/vehicles")
                .param("minLat", "-41") // Only providing one spatial parameter
                .contentType(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2)) // Confirm that its not filtering
                .andExpect(jsonPath("$[0].id").value("1"));

        mockMvc.perform(get("/api/vehicles")
                .param("maxLat", "-40") // Only providing one spatial parameter
                .contentType(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2)) // Confirm that its not filtering
                .andExpect(jsonPath("$[0].id").value("1"));

        mockMvc.perform(get("/api/vehicles")
                .param("minLon", "171") // Only providing one spatial parameter
                .contentType(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2)) // Confirm that its not filtering
                .andExpect(jsonPath("$[0].id").value("1"));

        mockMvc.perform(get("/api/vehicles")
                .param("maxLon", "172") // Only providing one spatial parameter
                .contentType(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2)) // Confirm that its not filtering
                .andExpect(jsonPath("$[0].id").value("1"));

        mockMvc.perform(get("/api/vehicles")
                .param("minLat", "-41")
                .param("maxLat", "-40")
                .contentType(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2)); // Confirm that its not filtering

        mockMvc.perform(get("/api/vehicles")
                .param("minLat", "-41")
                .param("maxLat", "-40")
                .param("minLon", "171")
                .contentType(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2)); // Confirm that its not filtering
    }
}
