package com.test.pollingProject.service;

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

import tools.jackson.databind.ObjectMapper;

@Service
public class GtfsRtService {
	private final VehicleRepository vehicleRepository;
	private final ObjectMapper objectMapper;

	private final RestClient restClient;

	public GtfsRtService(VehicleRepository vehicleRepository,
			ObjectMapper objectMapper,

			@Value("${GTFS_API_KEY}") String apiKey,
			@Value("${GTFS_API_URL}") String apiUrl) {
		this.vehicleRepository = vehicleRepository;
		this.objectMapper = objectMapper;

		this.restClient = RestClient.builder()
				.baseUrl(apiUrl)
				.defaultHeader("X-API-Key", apiKey)
				.build();
	}

	/**
	 * Polls the GTFS API for live vehicle data, and saves it to the database.
	 * 
	 * @apiNote Will retry up to 3 times before giving up and returning.
	 */
	@Retryable(includes = { ResourceAccessException.class,
			RestClientException.class }, maxRetries = 3, delay = 1000, multiplier = 2.0)
	public void pollAndSaveVehicles() {
		String jsonResponse = restClient.get()
				.uri("/gtfs-rt/vehiclepositions")
				.retrieve()
				.body(String.class); // Grab JSON API response
		GtfsRtDTO.Response response = objectMapper.readValue(jsonResponse, GtfsRtDTO.Response.class);
		List<Vehicle> vehicles = response.entity.stream()
				.map(this::toEntity)
				.collect(Collectors.toList());

		vehicleRepository.saveAll(vehicles);
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
		return Vehicle.builder()
				.id(vehicleDTO.map(v -> v.vehicle).map(v -> v.id).orElse("UNKNOWN"))
				.route_id(vehicleDTO.map(v -> v.trip).map(t -> t.route_id).orElse("UNKNOWN"))
				.latitude(vehicleDTO.map(v -> v.position).map(p -> p.latitude).orElse(0.0))
				.longitude(vehicleDTO.map(v -> v.position).map(p -> p.longitude).orElse(0.0))
				.bearing(vehicleDTO.map(v -> v.position).map(p -> p.bearing).orElse(0.0))
				.occupancy_status(vehicleDTO.map(v -> v.occupancy_status)
						.orElse(OccupancyStatus.NO_DATA_AVAILABLE))
				.build();
	}
}
