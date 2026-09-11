package com.test.pollingProject.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.test.pollingProject.model.Stop;
import com.test.pollingProject.model.enums.stops.LocationType;
import com.test.pollingProject.model.enums.stops.WheelchairBoarding;
import com.test.pollingProject.store.StopRepository;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;
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
    public List<Stop> getStops(
            @RequestParam(required = false) Double minLat,
            @RequestParam(required = false) Double maxLat,
            @RequestParam(required = false) Double minLon,
            @RequestParam(required = false) Double maxLon,
            @RequestParam(required = false) String wheelchairBoarding,
            @RequestParam(required = false) String locationType) {

        // Implementing a specification to dynamically filter based on what parameters
        // were provided
        Specification<Stop> spec = Specification.unrestricted();

        if (minLat != null && maxLat != null && minLon != null && maxLon != null) { // Find stops in bounds
            spec = spec.and((stop, query, cb) -> cb.between(stop.get("latitude"), minLat, maxLat));
            spec = spec.and((stop, query, cb) -> cb.between(stop.get("longitude"), minLon, maxLon));
        } else if (minLat != null || maxLat != null || minLon != null || maxLon != null) {
            System.out.println("API Call didnt specify all 4 Lat/Lon coordinates, ignoring request.");
        }
        if (wheelchairBoarding != null) {
            spec = spec.and((stop, query, cb) -> cb.equal(stop.get("wheelchairBoarding"),
                    WheelchairBoarding.fromLabel(wheelchairBoarding)));
        }
        if (locationType != null) {
            spec = spec.and((stop, query, cb) -> cb.equal(stop.get("locationType"),
                    LocationType.fromLabel(locationType)));
        }
        return stopRepository.findAll(spec);
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
