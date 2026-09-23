package com.test.pollingProject.store;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import com.test.pollingProject.model.Stop;
import static org.assertj.core.api.Assertions.assertThat;

import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;

// Runs on a docker container test database - requires docker daemon running

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = Replace.NONE)
class StopRepositoryTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:14-alpine");

    @Autowired
    private StopRepository stopRepository;

    @Test
    void testSaveAndFindStop() {
        Stop stop = new Stop();
        stop.setId("123");
        stop.setName("Test Station 1");

        stopRepository.save(stop);

        Stop found = stopRepository.findById("123").orElse(null);
        assertThat(found).isNotNull();
        assertThat(found.getName()).isEqualTo("Test Station 1");
    }
}
