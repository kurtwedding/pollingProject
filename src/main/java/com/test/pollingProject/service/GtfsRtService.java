package com.test.pollingProject.service;

import com.test.pollingProject.store.RouteRepository;
import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.resilience.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import com.test.pollingProject.model.GtfsRtDTO;
import com.test.pollingProject.model.Vehicle;
import com.test.pollingProject.model.enums.vehicles.OccupancyStatus;
import com.test.pollingProject.store.VehicleRepository;

import lombok.extern.slf4j.Slf4j;
import tools.jackson.databind.ObjectMapper;

@Slf4j
@Service
public class GtfsRtService {
	private final RouteRepository routeRepository;
	private final VehicleRepository vehicleRepository;
	private final ObjectMapper objectMapper;

	private final RestClient restClient;

	public GtfsRtService(VehicleRepository vehicleRepository,
			ObjectMapper objectMapper,
			RestClient.Builder builder,
			@Value("${GTFS_API_KEY}") String apiKey,
			@Value("${GTFS_API_URL}") String apiUrl,
			RouteRepository routeRepository) {
		this.vehicleRepository = vehicleRepository;
		this.objectMapper = objectMapper;
		this.routeRepository = routeRepository;

		this.restClient = builder
				.baseUrl(apiUrl)
				.defaultHeader("X-API-Key", apiKey)
				.build();
	}

	/**
	 * Polls the GTFS API for live vehicle data, and saves it to the database.
	 * 
	 * @apiNote Will retry up to 3 times before giving up and returning.
	 * 
	 */
	@Retryable(includes = { ResourceAccessException.class,
			RestClientException.class }, maxRetries = 3, delay = 1000, multiplier = 2.0)
	public void pollAndSaveVehicles() {
		long start = System.nanoTime();
		try {
			String jsonResponse = restClient.get()
					.uri("/gtfs-rt/vehiclepositions")
					.retrieve()
					.body(String.class); // Grab JSON API response
			GtfsRtDTO.Response response = objectMapper.readValue(jsonResponse, GtfsRtDTO.Response.class);
			List<Vehicle> vehicles = response.entity.stream()
					.map(this::toEntity)
					.collect(Collectors.toList());

			vehicleRepository.saveAll(vehicles);

			log.info("Poll ok feed=Vehicle records={} durationMs={}",
					vehicles.size(), Duration.ofNanos(System.nanoTime() - start).toMillis());

		} catch (RestClientException e) {
			log.warn("Poll attempt failed feed=Vehicle durationMs={}; {}",
					Duration.ofNanos(System.nanoTime() - start).toMillis(), e.getMessage());
			throw e;
		}
	}

	/**
	 * Maps the returned JSON fields to a DTO entity so that it can be stored in the
	 * database with little hassle
	 * 
	 * @param entity - The JSON Response to be mapped
	 * @return The mapped Java object
	 */
	private Vehicle toEntity(GtfsRtDTO.Entity entity) {

		Optional<GtfsRtDTO.VehicleExterior> vehicleDTO = Optional.ofNullable(entity.vehicle);

		Vehicle vehicle = new Vehicle();

		vehicle.setId(vehicleDTO.map(v -> v.vehicle).map(v -> v.id).orElse("UNKNOWN"));
		vehicle.setLatitude(vehicleDTO.map(v -> v.position).map(p -> p.latitude).orElse(0.0));
		vehicle.setLongitude(vehicleDTO.map(v -> v.position).map(p -> p.longitude).orElse(0.0));
		vehicle.setBearing(vehicleDTO.map(v -> v.position).map(p -> p.bearing).orElse(0.0));
		vehicle.setRoute(routeRepository.getReferenceById(
				vehicleDTO.map(v -> v.trip).map(t -> t.route_id).orElse("UNKNOWN")));
		vehicle.setOccupancyStatus(vehicleDTO.map(v -> v.occupancy_status)
				.orElse(OccupancyStatus.NO_DATA_AVAILABLE));

		return vehicle;
	}
}
