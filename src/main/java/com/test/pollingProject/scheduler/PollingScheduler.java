package com.test.pollingProject.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import com.test.pollingProject.model.Route;
import com.test.pollingProject.model.Stop;
import com.test.pollingProject.model.Trip;
import com.test.pollingProject.store.RouteRepository;
import com.test.pollingProject.store.StopRepository;
import com.test.pollingProject.store.TripRepository;

import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@Component
public class PollingScheduler {

    private final ObjectMapper objectMapper;
    private final StopRepository stopRepository;
    private final RouteRepository routeRepository;
    private final TripRepository tripRepository;

    private final RestClient restClient;

    public PollingScheduler(ObjectMapper objectMapper, StopRepository stopRepository,
            RouteRepository routeRepository, TripRepository tripRepository) {
        this.objectMapper = objectMapper;
        this.stopRepository = stopRepository;
        this.routeRepository = routeRepository;
        this.tripRepository = tripRepository;
        String apiKey = System.getProperty("GTFS_API_KEY");
        this.restClient = RestClient.builder()
                .baseUrl(System.getProperty("GTFS_API_URL"))
                .defaultHeader("X-API-Key", apiKey)
                .build();
    }

    @Scheduled(fixedRateString = "P1D") // Once a day
    private void getStaticGTFSData() throws Exception {
        System.out.println(
                "[" + LocalTime.now() + "] Polling for Static GTFS Data...");
        try {
            String jsonResponse = restClient.get()
                    .uri("/gtfs/stops")
                    .retrieve()
                    .body(String.class); // Grab JSON API response
            List<Stop> stops = objectMapper.readValue(jsonResponse, new TypeReference<List<Stop>>() {
            }); // Map it to a list of Stop objects

            stopRepository.saveAll(stops); // Save the stops to the Postgres Database table
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        try {
            String jsonResponse = restClient.get()
                    .uri("/gtfs/routes")
                    .retrieve()
                    .body(String.class); // Grab JSON API response
            List<Route> routes = objectMapper.readValue(jsonResponse, new TypeReference<List<Route>>() {
            }); // Map it to a list of Route objects

            routeRepository.saveAll(routes); // Save the routes to the Postgres Database table
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        try {
            String jsonResponse = restClient.get()
                    .uri("/gtfs/trips")
                    .retrieve()
                    .body(String.class); // Grab JSON API response
            List<Trip> trips = objectMapper.readValue(jsonResponse, new TypeReference<List<Trip>>() {
            }); // Map it to a list of Trip objects

            tripRepository.saveAll(trips); // Save the routes to the Postgres Database table
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Scheduled(fixedRateString = "30s")
    private void getLiveGTFSData() {
        System.out.println(
                "[" + LocalTime.now() + "] Polling for Realtime GTFS Data...");

    }

    public List<Map<String, Object>> convertToListOfMaps(String json) throws Exception {
        return objectMapper.readValue(json, new TypeReference<List<Map<String, Object>>>() {
        });
    }

}
