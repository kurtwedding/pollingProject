package com.test.pollingProject.service;

import com.test.pollingProject.model.Stop;
import com.test.pollingProject.store.StopRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DataService {

    private final StopRepository stopRepository;

    public DataService(StopRepository stopRepository) {
        this.stopRepository = stopRepository;
    }

    public List<Stop> getData() {
        return stopRepository.findAll();
    }
}
