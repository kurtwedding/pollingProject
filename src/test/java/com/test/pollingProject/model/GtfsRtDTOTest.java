package com.test.pollingProject.model;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.test.pollingProject.model.enums.vehicles.OccupancyStatus;

import static org.junit.jupiter.api.Assertions.*;

class GtfsRtDTOTest {

    private final ObjectMapper objectMapper = new JsonMapper();

    @Test
    void shouldDeserialiseGTFSResponse() throws Exception {

        /*
         * Test json object for a full GTFSRT response
         * String json = """
         * {
         * "entity": [
         * {
         * "vehicle": {
         * "timestamp": 1,
         * "position": {
         * "latitude": -41.8574,
         * "longitude": 171.3342,
         * "bearing": 135.0
         * },
         * "vehicle": {
         * "id": "TEST_BUS"
         * },
         * "trip": {
         * "route_id": "TEST_ROUTE",
         * "schedule_relationship": 0,
         * "start_date": "19700101",
         * "trip_id": "TEST_TRIP"
         * "start_time": "00:00:01",
         * "direction_id": 0
         * },
         * "occupancy_status": "MANY_SEATS_AVAILABLE"
         * },
         * "id": "VEHICLE_ID"
         * }
         * ]
         * }
         * """;
         */

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

        GtfsRtDTO.Response response = objectMapper.readValue(json, GtfsRtDTO.Response.class);

        // ensure response.entity exists
        assertNotNull(response.entity);
        assertEquals(1, response.entity.size());

        // Verify entity, and exterior vehicle object
        GtfsRtDTO.Entity entity = response.entity.get(0);
        assertNotNull(entity.vehicle);

        assertEquals(-41.8574, entity.vehicle.position.latitude);
        assertEquals(171.3342, entity.vehicle.position.longitude);
        assertEquals(135.0, entity.vehicle.position.bearing);

        assertEquals("TEST_BUS", entity.vehicle.vehicle.id);

        assertEquals("TEST_ROUTE", entity.vehicle.trip.route_id);

        assertEquals(OccupancyStatus.MANY_SEATS_AVAILABLE, entity.vehicle.occupancy_status);
    }

    @Test
    void shouldInstantiateOuterClass() {
        assertNotNull(new GtfsRtDTO());
    }
}
