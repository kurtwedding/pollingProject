package com.test.pollingProject.scheduler;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.repository.CrudRepository;
import org.springframework.resilience.annotation.Retryable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import com.test.pollingProject.model.Route;
import com.test.pollingProject.model.Stop;
import com.test.pollingProject.model.Trip;
import com.test.pollingProject.service.GtfsRtService;
import com.test.pollingProject.store.RouteRepository;
import com.test.pollingProject.store.StopRepository;
import com.test.pollingProject.store.TripRepository;

import lombok.extern.slf4j.Slf4j;
import tools.jackson.databind.ObjectMapper;

import java.time.Duration;
import java.util.List;

/**
 * PollingScheduler defines the periodic running of scripts to keep the database
 * populated with up-to-date information, based on the GTFS provider's updating
 * schedule on data.
 */
@Slf4j
@Component
@ConditionalOnProperty(name = "polling.enabled", havingValue = "true", matchIfMissing = true)
public class PollingScheduler {

	private final ObjectMapper objectMapper;
	private final StopRepository stopRepository;
	private final RouteRepository routeRepository;
	private final TripRepository tripRepository;

	private final GtfsRtService gtfsRtService;

	private final RestClient restClient;

	public PollingScheduler(ObjectMapper objectMapper,
			StopRepository stopRepository,
			RouteRepository routeRepository,
			TripRepository tripRepository,
			GtfsRtService gtfsRtService,

			@Value("${GTFS_API_KEY}") String apiKey,
			@Value("${GTFS_API_URL}") String apiUrl) {
		this.objectMapper = objectMapper;
		this.stopRepository = stopRepository;
		this.routeRepository = routeRepository;
		this.tripRepository = tripRepository;
		this.gtfsRtService = gtfsRtService;

		this.restClient = RestClient.builder()
				.baseUrl(apiUrl)
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
	@Scheduled(fixedRateString = "P${STATIC_POLLING_INTERVAL:1d}") // Once a day
	void getStaticGTFSData() throws Exception {

		// Polling Stops Data
		try {
			getStaticDataFromEndpoint("/gtfs/stops", Stop.class, stopRepository);
		} catch (RuntimeException e) {
			log.error("poll failed feed=stops", e);
		}

		// Polling Routes Data
		try {
			getStaticDataFromEndpoint("/gtfs/routes", Route.class, routeRepository);
		} catch (RuntimeException e) {
			log.error("poll failed feed=routes", e);
		}

		// Polling Trips Data
		try {
			getStaticDataFromEndpoint("/gtfs/trips", Trip.class, tripRepository);
		} catch (RuntimeException e) {
			log.error("Poll failed feed=trips", e);
		}
	}

	@Retryable(includes = { ResourceAccessException.class,
			RestClientException.class }, maxRetries = 3, delay = 1000, multiplier = 2.0)
	<T> void getStaticDataFromEndpoint(String uri, Class<T> cl, CrudRepository<T, ?> repository) {

		log.debug("Polling static gtfs feed={}", cl.getSimpleName());
		long start = System.nanoTime();

		try {
			String jsonResponse = restClient.get()
					.uri(uri)
					.retrieve()
					.body(String.class); // Grab JSON API response
			List<T> objects = objectMapper.readValue(jsonResponse,
					objectMapper.getTypeFactory().constructCollectionType(List.class, cl));
			repository.saveAll(objects);

			log.info("Poll ok feed={} records={} durationMs={}",
					cl.getSimpleName(), objects.size(),
					Duration.ofNanos(System.nanoTime() - start).toMillis());
		} catch (RestClientException e) {
			log.warn("Poll attempt failed feed={} durationMs={}; {}",
					cl.getSimpleName(), Duration.ofNanos(System.nanoTime() - start).toMillis(), e.getMessage());
			throw e;
		}
	}

	/**
	 * Runs every 30s to poll the GTFS API link for vehicle positions and store them
	 * inside the database.
	 * 
	 * @throws RuntimeException If the RTFS realtime data is unable to be polled
	 *                          successfully
	 * @see #getStaticGTFSData()
	 */
	@Scheduled(fixedRateString = "${LIVE_POLLING_INTERVAL:30s}")
	void getLiveGTFSData() {

		log.debug("Polling live gtfs-rt");
		long start = System.nanoTime();

		try {
			gtfsRtService.pollAndSaveVehicles();
		} catch (RuntimeException e) {
			log.error("Poll failed feed=Vehicle durationMs={}",
					Duration.ofNanos(System.nanoTime() - start).toMillis(), e);
		}
	}
}
