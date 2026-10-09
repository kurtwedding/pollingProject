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
import com.test.pollingProject.model.Trip;
import com.test.pollingProject.model.enums.trips.BikesAllowed;
import com.test.pollingProject.model.enums.trips.Direction;
import com.test.pollingProject.model.enums.trips.WheelchairAccessible;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Import(TestcontainersConfiguration.class)
public class TripRepositoryTest {

    @Autowired
    private TripRepository tripRepository;

    @Test
    void shouldSaveAndFindTrip() {
        Trip trip = new Trip();
        trip.setTrip_id("1");
        trip.setRoute_id("123");
        trip.setService_id("999");
        trip.setShape_id("404");
        trip.setTrip_headsign("TESTTRIP");
        trip.setOrigin_stop_id("101");
        trip.setDestination_stop_id("110");
        trip.setDirection_id(Direction.INBOUND);
        trip.setBikes_allowed(BikesAllowed.ALLOWED);
        trip.setWheelchair_accessible(WheelchairAccessible.ACCESSIBLE);

        Trip saved = tripRepository.save(trip);

        Trip found = tripRepository.findById("1").orElse(null);
        assertThat(found).isNotNull();
        assertThat(found.getTrip_id()).isEqualTo(saved.getTrip_id());
        assertThat(found.getRoute_id()).isEqualTo("123");
        assertThat(found.getService_id()).isEqualTo("999");
        assertThat(found.getShape_id()).isEqualTo("404");
        assertThat(found.getTrip_headsign()).isEqualTo("TESTTRIP");
        assertThat(found.getOrigin_stop_id()).isEqualTo("101");
        assertThat(found.getDestination_stop_id()).isEqualTo("110");
        assertThat(found.getDirection_id()).isEqualTo(Direction.INBOUND);
        assertThat(found.getBikes_allowed()).isEqualTo(BikesAllowed.ALLOWED);
        assertThat(found.getWheelchair_accessible()).isEqualTo(WheelchairAccessible.ACCESSIBLE);
    }
}
