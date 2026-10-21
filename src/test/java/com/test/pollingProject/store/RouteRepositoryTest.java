package com.test.pollingProject.store;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase.Replace;
import org.springframework.context.annotation.Import;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.test.pollingProject.TestcontainersConfiguration;
import com.test.pollingProject.model.Route;
import com.test.pollingProject.model.enums.routes.RouteType;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Import(TestcontainersConfiguration.class)
public class RouteRepositoryTest {

    @Autowired
    private RouteRepository routeRepository;

    @Test
    void shouldSaveAndFindRoute() {
        Route route = new Route();
        route.setRouteId("123");
        route.setAgencyId("TSTA");
        route.setRouteShortName("TR1");
        route.setRouteLongName("Test Route 1");
        route.setRoute_color("#FFFFFF");
        route.setRoute_text_color("#000000");
        route.setRouteType(RouteType.BUS);

        Route saved = routeRepository.save(route);

        Route found = routeRepository.findById("123").orElse(null);
        assertThat(found).isNotNull();
        assertThat(found.getRouteId()).isEqualTo(saved.getRouteId());
        assertThat(found.getAgencyId()).isEqualTo("TSTA");
        assertThat(found.getRouteShortName()).isEqualTo("TR1");
        assertThat(found.getRouteLongName()).isEqualTo("Test Route 1");
        assertThat(found.getRoute_color()).isEqualTo("#FFFFFF");
        assertThat(found.getRoute_text_color()).isEqualTo("#000000");
        assertThat(found.getRouteType()).isEqualTo(RouteType.BUS);
    }
}
