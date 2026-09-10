package com.test.pollingProject.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import com.test.pollingProject.model.Route;
import com.test.pollingProject.model.Stop;
import com.test.pollingProject.model.Trip;
import com.test.pollingProject.service.GtfsRtService;
import com.test.pollingProject.store.RouteRepository;
import com.test.pollingProject.store.StopRepository;
import com.test.pollingProject.store.TripRepository;
import com.test.pollingProject.store.VehicleRepository;

import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.time.Duration;
import java.time.LocalTime;
import java.util.List;

/**
 * PollingScheduler defines the periodic running of scripts to keep the database
 * populated with up-to-date information, based on the GTFS provider's updating
 * schedule on data.
 */
@Component
public class PollingScheduler {

    private final ObjectMapper objectMapper;
    private final StopRepository stopRepository;
    private final RouteRepository routeRepository;
    private final TripRepository tripRepository;
    private final VehicleRepository vehicleRepository;

    private final RestClient restClient;

    public PollingScheduler(ObjectMapper objectMapper, StopRepository stopRepository,
            RouteRepository routeRepository, TripRepository tripRepository, VehicleRepository vehicleRepository) {
        this.objectMapper = objectMapper;
        this.stopRepository = stopRepository;
        this.routeRepository = routeRepository;
        this.tripRepository = tripRepository;
        this.vehicleRepository = vehicleRepository;

        String apiKey = System.getProperty("GTFS_API_KEY");
        this.restClient = RestClient.builder()
                .baseUrl(System.getProperty("GTFS_API_URL"))
                .defaultHeader("X-API-Key", apiKey)
                .build();
    }

    /**
     * This section runs once a day, to poll for updates in the Static data, trips,
     * routes, and stops.
     * 
     * @throws Exception If one of the api endpoints does not return the correct
     *                   data
     * @see #getLiveGTFSData()
     */
    @Scheduled(fixedRateString = "P1D") // Once a day
    private void getStaticGTFSData() throws Exception {
        System.out.println(
                "[" + LocalTime.now() + "] Polling for Static GTFS Data...");
        try {
            LocalTime tempTime = LocalTime.now();
            String jsonResponse = restClient.get()
                    .uri("/gtfs/stops")
                    .retrieve()
                    .body(String.class); // Grab JSON API response
            List<Stop> stops = objectMapper.readValue(jsonResponse, new TypeReference<List<Stop>>() {
            });

            stopRepository.saveAll(stops); // Save the stops to the Database table
            System.out.println(
                    "[" + LocalTime.now() + "] Successfully polled stops data in: "
                            + (Duration.between(tempTime, LocalTime.now()).toMillis()) + " milliseconds");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        try {
            LocalTime tempTime = LocalTime.now();
            String jsonResponse = restClient.get()
                    .uri("/gtfs/routes")
                    .retrieve()
                    .body(String.class); // Grab JSON API response
            List<Route> routes = objectMapper.readValue(jsonResponse, new TypeReference<List<Route>>() {
            });

            routeRepository.saveAll(routes); // Save the routes to the Database table
            System.out.println(
                    "[" + LocalTime.now() + "] Successfully polled routes data in: "
                            + (Duration.between(tempTime, LocalTime.now()).toMillis()) + " milliseconds");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        try {
            LocalTime tempTime = LocalTime.now();
            String jsonResponse = restClient.get()
                    .uri("/gtfs/trips")
                    .retrieve()
                    .body(String.class); // Grab JSON API response
            List<Trip> trips = objectMapper.readValue(jsonResponse, new TypeReference<List<Trip>>() {
            });

            tripRepository.saveAll(trips); // Save the routes to the Database table
            System.out.println(
                    "[" + LocalTime.now() + "] Successfully polled trips data in: "
                            + (Duration.between(tempTime, LocalTime.now()).toMillis()) + " milliseconds");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Runs once a day to poll the GTFS API link for vehicle positions and store it
     * inside the database.
     * 
     * @throws RuntimeException If the RTFS realtime data is unable to be polled
     *                          successfully
     * @see #getStaticGTFSData()
     */
    @Scheduled(fixedRateString = "30s")
    private void getLiveGTFSData() {
        GtfsRtService gtfsRtService = new GtfsRtService(vehicleRepository, objectMapper);

        LocalTime tempTime = LocalTime.now();
        System.out.println(
                "[" + LocalTime.now() + "] Polling for Realtime GTFS Data...");
        try {
            gtfsRtService.pollAndSaveVehicles();
            System.out.println(
                    "[" + LocalTime.now() + "] Successfully polled data in: "
                            + (Duration.between(tempTime, LocalTime.now()).toMillis()) + " milliseconds");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
