package com.test.pollingProject.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.test.pollingProject.model.Vehicle;
import com.test.pollingProject.model.enums.vehicles.OccupancyStatus;
import com.test.pollingProject.store.VehicleRepository;

import lombok.extern.slf4j.Slf4j;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@RestController
@RequestMapping("/api/vehicles")
public class VehicleController {

    private final VehicleRepository vehicleRepository;

    public VehicleController(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    @GetMapping
    public List<Vehicle> getVehicles(
            @RequestParam(required = false) Double minLat,
            @RequestParam(required = false) Double maxLat,
            @RequestParam(required = false) Double minLon,
            @RequestParam(required = false) Double maxLon,
            @RequestParam(required = false) String routeId,
            @RequestParam(required = false) String occupancy) {

        Specification<Vehicle> spec = Specification.unrestricted();

        if (minLat != null && maxLat != null && minLon != null && maxLon != null) { // Find stops in bounds
            spec = spec.and((stop, query, cb) -> cb.between(stop.get("latitude"), minLat, maxLat));
            spec = spec.and((stop, query, cb) -> cb.between(stop.get("longitude"), minLon, maxLon));
        } else if (minLat != null || maxLat != null || minLon != null || maxLon != null) {
            log.debug("/api/vehicles api call didnt specify all 4 lat/lon coordinates, ignoring request.");
        }
        if (routeId != null) {
            spec = spec.and((stop, query, cb) -> cb.equal(stop.get("route").get("routeId"), routeId));
        }
        if (occupancy != null) {
            spec = spec.and((stop, query, cb) -> cb.equal(stop.get("occupancyStatus"),
                    OccupancyStatus.fromLabel(occupancy)));
        }

        List<Vehicle> results = vehicleRepository.findAll(spec);

        log.debug("api call made feed=Vehicle records={}",
                results.size());

        return results;
    }

}
