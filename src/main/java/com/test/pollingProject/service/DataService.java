package com.test.pollingProject.service;

import com.test.pollingProject.model.Stop;
import com.test.pollingProject.scheduler.PollingScheduler;
import com.test.pollingProject.store.StopRepository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DataService {

    private final StopRepository stopRepository;
    private final PollingScheduler pollingScheduler;

    @Value("${GTFS_API_KEY}")
    private String apiKey;

    public DataService(PollingScheduler pollingScheduler, StopRepository stopRepository) {
        this.pollingScheduler = pollingScheduler;
        this.stopRepository = stopRepository;
    }

    public List<Stop> getData() {
        return stopRepository.findAll();
    }
}
