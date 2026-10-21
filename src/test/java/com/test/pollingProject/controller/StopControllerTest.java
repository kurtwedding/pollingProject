package com.test.pollingProject.controller;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import com.test.pollingProject.model.Stop;
import com.test.pollingProject.model.enums.stops.LocationType;
import com.test.pollingProject.model.enums.stops.WheelchairBoarding;
import com.test.pollingProject.store.StopRepository;

@WebMvcTest(StopController.class)
@AutoConfigureMockMvc(addFilters = false)
public class StopControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private StopRepository stopRepository;

    private Stop mockStop1;
    private Stop mockStop2;

    private List<Stop> mockStops;

    @BeforeEach
    void setUp() {
        mockStop1 = new Stop();
        mockStop1.setId("1");
        mockStop1.setLatitude(-40.5);
        mockStop1.setLongitude(171.5);
        mockStop1.setLocationType(LocationType.STOP);
        mockStop1.setName("test stop 1");
        mockStop1.setWheelchairBoarding(WheelchairBoarding.ACCESSIBLE);

        mockStop2 = new Stop();
        mockStop2.setId("2");
        mockStop2.setLatitude(40.5);
        mockStop2.setLongitude(-171.5);
        mockStop2.setLocationType(LocationType.STATION);
        mockStop2.setName("test stop 2");
        // Don't set WheelchairBoarding here, will default to "UNKNOWN"

        mockStops = Arrays.asList(mockStop1, mockStop2);
    }

    @SuppressWarnings("unchecked")
    @Test
    void shouldReturnAllStopsWhenNoParams() throws Exception {

        Mockito.when(stopRepository.findAll(any(Specification.class)))
                .thenReturn(mockStops);
        mockMvc.perform(get("/api/stops")
                .contentType(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(mockStops.size()))
                .andExpect(jsonPath("$[0].stop_name").value("test stop 1"));
    }

    @SuppressWarnings("unchecked")
    @Test
    void shouldFilterStopsByWheelchairBoarding() throws Exception {

        Mockito.when(stopRepository.findAll(any(Specification.class)))
                .thenReturn(Arrays.asList(mockStop1));

        mockMvc.perform(get("/api/stops")
                .param("wheelchairBoarding", "accessible")
                .contentType(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].wheelchair_boarding").value("ACCESSIBLE"));
    }

    @SuppressWarnings("unchecked")
    @Test
    void shouldFilterStopsByLocationType() throws Exception {

        Mockito.when(stopRepository.findAll(any(Specification.class)))
                .thenReturn(Arrays.asList(mockStop1));

        mockMvc.perform(get("/api/stops")
                .param("locationType", "stop")
                .contentType(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].location_type").value("STOP"));
    }

    @SuppressWarnings("unchecked")
    @Test
    void shouldFilterStopsByLatLonBounds() throws Exception {

        Mockito.when(stopRepository.findAll(any(Specification.class)))
                .thenReturn(Arrays.asList(mockStop1));

        mockMvc.perform(get("/api/stops")
                .param("minLat", "-41")
                .param("maxLat", "-40")
                .param("minLon", "171")
                .param("maxLon", "172")
                .contentType(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].stop_lat").value("-40.5"))
                .andExpect(jsonPath("$[0].stop_lon").value("171.5"));

    }

    @SuppressWarnings("unchecked")
    @Test
    void shouldIgnoreFilterIfLessThanFourSpatialParamsProvided() throws Exception {

        // This test covers all 4 branches, if only one parameter is provided

        Mockito.when(stopRepository.findAll(any(Specification.class)))
                .thenReturn(mockStops);

        mockMvc.perform(get("/api/stops")
                .param("minLat", "-41") // Only providing one spatial parameter
                .contentType(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2)); // Confirm that its not filtering

        mockMvc.perform(get("/api/stops")
                .param("maxLat", "-40") // Only providing one spatial parameter
                .contentType(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2)); // Confirm that its not filtering

        mockMvc.perform(get("/api/stops")
                .param("minLon", "171") // Only providing one spatial parameter
                .contentType(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2)); // Confirm that its not filtering

        mockMvc.perform(get("/api/stops")
                .param("maxLon", "172") // Only providing one spatial parameter
                .contentType(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2)); // Confirm that its not filtering

        mockMvc.perform(get("/api/stops")
                .param("minLat", "-41")
                .param("maxLat", "-40")
                .contentType(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2)); // Confirm that its not filtering

        mockMvc.perform(get("/api/stops")
                .param("minLat", "-41")
                .param("maxLat", "-40")
                .param("minLon", "171")
                .contentType(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2)); // Confirm that its not filtering
    }
}
