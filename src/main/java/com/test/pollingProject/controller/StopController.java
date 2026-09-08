package com.test.pollingProject.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.test.pollingProject.model.Stop;
import com.test.pollingProject.store.StopRepository;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/stops")
public class StopController {

    private final StopRepository stopRepository;

    public StopController(StopRepository stopRepository) {
        this.stopRepository = stopRepository;
    }

    @GetMapping()
    public List<Stop> getAllStops() {
        return stopRepository.findAll();
    }

    @GetMapping("/bounds")
    public List<Stop> getStopsInBounds(
            @RequestParam double minLat,
            @RequestParam double maxLat,
            @RequestParam double minLon,
            @RequestParam double maxLon) {
        return stopRepository.findByLatitudeBetweenAndLongitudeBetween(minLat, maxLat, minLon, maxLon);
    }

}
