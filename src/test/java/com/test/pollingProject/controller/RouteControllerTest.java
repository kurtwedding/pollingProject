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

import com.test.pollingProject.model.Route;
import com.test.pollingProject.model.enums.routes.RouteType;
import com.test.pollingProject.store.RouteRepository;

@WebMvcTest(RouteController.class)
@AutoConfigureMockMvc(addFilters = false)
public class RouteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RouteRepository routeRepository;

    private Route mockRoute1;
    private Route mockRoute2;

    private List<Route> mockRoutes;

    @BeforeEach
    void setUp() {
        mockRoute1 = new Route();
        mockRoute1.setRouteId("1");
        mockRoute1.setRouteShortName("TR1");
        mockRoute1.setRouteType(RouteType.BUS);
        mockRoute1.setAgencyId("TST1");

        mockRoute2 = new Route();
        mockRoute2.setRouteId("2");
        mockRoute2.setRouteShortName("TR2");
        mockRoute2.setRouteType(RouteType.TRAM);
        mockRoute2.setAgencyId("TST2");

        mockRoutes = Arrays.asList(mockRoute1, mockRoute2);
    }

    @SuppressWarnings("unchecked")
    @Test
    void shouldReturnAllRoutesWhenNoParams() throws Exception {

        Mockito.when(routeRepository.findAll(any(Specification.class)))
                .thenReturn(mockRoutes);

        mockMvc.perform(get("/api/routes")
                .contentType(MediaType.APPLICATION_JSON))

                // Assertions
                .andExpect(status().isOk()) // check that it responded ok
                .andExpect(jsonPath("$.size()").value(mockRoutes.size()))
                .andExpect(jsonPath("$[0].route_id").value(1))
                .andExpect(jsonPath("$[0].route_short_name").value("TR1"));
    }

    @SuppressWarnings("unchecked")
    @Test
    void shouldFilterRoutesByRouteId() throws Exception {

        Mockito.when(routeRepository.findAll(any(Specification.class)))
                .thenReturn(Arrays.asList(mockRoute1));

        mockMvc.perform(get("/api/routes")
                .param("id", "1")
                .contentType(MediaType.APPLICATION_JSON))

                // Assertions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].route_id").value(1));
    }

    @SuppressWarnings("unchecked")
    @Test
    void shouldFilterRoutesByShortName() throws Exception {

        Mockito.when(routeRepository.findAll(any(Specification.class)))
                .thenReturn(Arrays.asList(mockRoute1));

        mockMvc.perform(get("/api/routes")
                .param("shortName", "TR1")
                .contentType(MediaType.APPLICATION_JSON))

                // Assertions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].route_short_name").value("TR1"));
    }

    @SuppressWarnings("unchecked")
    @Test
    void shouldFilterRoutesByRouteType() throws Exception {
        Mockito.when(routeRepository.findAll(any(Specification.class)))
                .thenReturn(Arrays.asList(mockRoute1));

        mockMvc.perform(get("/api/routes")
                .param("routeType", "bus")
                .contentType(MediaType.APPLICATION_JSON))

                // Assertions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].route_type").value("BUS"));
    }

    @SuppressWarnings("unchecked")
    @Test
    void shouldFilterRoutesByAgencyId() throws Exception {
        Mockito.when(routeRepository.findAll(any(Specification.class)))
                .thenReturn(Arrays.asList(mockRoute1));

        mockMvc.perform(get("/api/routes")
                .param("agencyId", "TST1")
                .contentType(MediaType.APPLICATION_JSON))

                // Assertions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].agency_id").value("TST1"));
    }

    @SuppressWarnings("unchecked")
    @Test
    void shouldFilterRoutesByMultipleParams() throws Exception {
        Mockito.when(routeRepository.findAll(any(Specification.class)))
                .thenReturn(Arrays.asList(mockRoute1));

        mockMvc.perform(get("/api/routes")
                .param("agencyId", "TST1")
                .param("routeShortName", "TR1")
                .contentType(MediaType.APPLICATION_JSON))

                // Assertions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].agency_id").value("TST1"))
                .andExpect(jsonPath("$[0].route_short_name").value("TR1"));
    }
}
