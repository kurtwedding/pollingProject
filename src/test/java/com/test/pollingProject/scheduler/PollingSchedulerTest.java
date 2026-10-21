package com.test.pollingProject.scheduler;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

import com.test.pollingProject.service.GtfsRtService;
import com.test.pollingProject.store.RouteRepository;
import com.test.pollingProject.store.StopRepository;
import com.test.pollingProject.store.TripRepository;

import tools.jackson.databind.ObjectMapper;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withServerError;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import org.junit.jupiter.api.BeforeEach;

@ExtendWith(MockitoExtension.class)
class PollingSchedulerTest {

	// Using real object to avoid a null exception in testing
	private ObjectMapper objectMapper = new tools.jackson.databind.json.JsonMapper();

	@Mock
	private StopRepository stopRepository;

	@Mock
	private RouteRepository routeRepository;

	@Mock
	private TripRepository tripRepository;

	@Mock
	private GtfsRtService gtfsRtService;

	private MockRestServiceServer mockRestServiceServer;

	private PollingScheduler pollingScheduler;

	@BeforeEach
	void setUp() {
		RestClient.Builder builder = RestClient.builder();
		mockRestServiceServer = MockRestServiceServer.bindTo(builder).build();
		// Create a polling scheduler with a testing api key and url
		pollingScheduler = new PollingScheduler(
				objectMapper,
				stopRepository,
				routeRepository,
				tripRepository,
				gtfsRtService,
				builder,
				"test-api-key",
				"http://test-api.com");
	}

	@Test
	void shouldGetLiveGTFSData() {
		pollingScheduler.getLiveGTFSData();

		verify(gtfsRtService).pollAndSaveVehicles();
	}

	@Test
	void shouldGetLiveGTFSDataThenThrowAndCatchException() {
		doThrow(new RuntimeException("API unavailable"))
				.when(gtfsRtService).pollAndSaveVehicles();

		assertDoesNotThrow(() -> pollingScheduler.getLiveGTFSData());
		verify(gtfsRtService, times(1)).pollAndSaveVehicles();
	}

	@Test
	void shouldGetStaticGTFSData() throws Exception {
		// Mock responses from endpoints
		mockRestServiceServer.expect(requestTo("http://test-api.com/gtfs/stops"))
				.andRespond(withSuccess("[]", MediaType.APPLICATION_JSON));
		mockRestServiceServer.expect(requestTo("http://test-api.com/gtfs/routes"))
				.andRespond(withSuccess("[]", MediaType.APPLICATION_JSON));
		mockRestServiceServer.expect(requestTo("http://test-api.com/gtfs/trips"))
				.andRespond(withSuccess("[]", MediaType.APPLICATION_JSON));

		// Poll the mock endpoints
		pollingScheduler.getStaticGTFSData();

		// Verify
		mockRestServiceServer.verify();
	}

	@Test
	void shouldGetStaticGTFSDataThenThrowAndCatchException() {
		// Mock responses from endpoints
		// Throw a failed response
		mockRestServiceServer.expect(requestTo("http://test-api.com/gtfs/stops"))
				.andRespond(withServerError());
		mockRestServiceServer.expect(requestTo("http://test-api.com/gtfs/routes"))
				.andRespond(withServerError());
		mockRestServiceServer.expect(requestTo("http://test-api.com/gtfs/trips"))
				.andRespond(withServerError());

		assertDoesNotThrow(() -> pollingScheduler.getStaticGTFSData());

		mockRestServiceServer.verify();
		verify(stopRepository, never()).saveAll(any());

	}
}
