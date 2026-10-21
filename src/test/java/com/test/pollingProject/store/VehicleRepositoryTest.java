package com.test.pollingProject.store;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase.Replace;
import org.springframework.context.annotation.Import;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.test.pollingProject.TestcontainersConfiguration;
import com.test.pollingProject.model.Route;
import com.test.pollingProject.model.Vehicle;
import com.test.pollingProject.model.enums.vehicles.OccupancyStatus;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Import(TestcontainersConfiguration.class)
public class VehicleRepositoryTest {

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private RouteRepository routeRepository;

    @Test
    void shouldSaveAndFindVehicle() {
        Vehicle vehicle = new Vehicle();
        vehicle.setId("44");
        vehicle.setBearing(90.0);
        vehicle.setLatitude(-50.0);
        vehicle.setLongitude(130.0);
        vehicle.setOccupancyStatus(OccupancyStatus.EMPTY);
        Route route = new Route();
        route.setRouteId("83");
        routeRepository.save(route);
        vehicle.setRoute(route);

        Vehicle saved = vehicleRepository.save(vehicle);

        Vehicle found = vehicleRepository.findById("44").orElse(null);

        assertThat(found).isNotNull();
        assertThat(found.getId()).isEqualTo(saved.getId());
        assertThat(found.getBearing()).isEqualTo(90.0);
        assertThat(found.getLatitude()).isEqualTo(-50.0);
        assertThat(found.getLongitude()).isEqualTo(130.0);
        assertThat(found.getOccupancyStatus()).isEqualTo(OccupancyStatus.EMPTY);
        assertThat(found.getRoute().getRouteId()).isEqualTo("83");

    }

    @Test
    void shouldReturnEmptyWhenNoLatitudeLongitudeMatch() {
        List<Vehicle> inBounds = vehicleRepository.findByLatitudeBetweenAndLongitudeBetween(-41.0, -40.0, 171.0, 172.0);

        assertThat(inBounds).isEmpty();
    }
}
