package com.test.pollingProject.store;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.test.pollingProject.TestcontainersConfiguration;
import com.test.pollingProject.model.Stop;
import com.test.pollingProject.model.enums.stops.LocationType;
import com.test.pollingProject.model.enums.stops.WheelchairBoarding;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase.Replace;
import org.springframework.context.annotation.Import;
import org.testcontainers.junit.jupiter.Testcontainers;

// Runs on a docker container test database - requires docker daemon running

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = Replace.NONE) // Tell spring to not use H2, instead use my postgres instance
@Import(TestcontainersConfiguration.class) // import postgres config
class StopRepositoryTest {

    @Autowired
    private StopRepository stopRepository;

    @Test
    void shouldSaveAndFindStop() {
        Stop stop = new Stop();
        stop.setId("123");
        stop.setName("Test Station 1");
        stop.setParentStation("Parent Station 1");
        stop.setLatitude(-40.5);
        stop.setLongitude(171.5);
        stop.setWheelchairBoarding(WheelchairBoarding.ACCESSIBLE);
        stop.setLocationType(LocationType.STOP);

        Stop saved = stopRepository.save(stop);

        Stop found = stopRepository.findById("123").orElse(null);
        assertThat(found).isNotNull();
        assertThat(found.getId()).isEqualTo(saved.getId());
        assertThat(found.getName()).isEqualTo("Test Station 1");
        assertThat(found.getParentStation()).isEqualTo("Parent Station 1");
        assertThat(found.getLatitude()).isEqualTo(-40.5);
        assertThat(found.getLongitude()).isEqualTo(171.5);
        assertThat(found.getWheelchairBoarding()).isEqualTo(WheelchairBoarding.ACCESSIBLE);
        assertThat(found.getLocationType()).isEqualTo(LocationType.STOP);
    }

    @Test
    void shouldReturnEmptyWhenNoLatitudeLongitudeMatch() {
        List<Stop> inBounds = stopRepository.findByLatitudeBetweenAndLongitudeBetween(-41.0, -40.0, 171.0, 172.0);

        assertThat(inBounds).isEmpty();
    }
}
