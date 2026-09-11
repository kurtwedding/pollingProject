package com.test.pollingProject.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.test.pollingProject.model.Vehicle;
import com.test.pollingProject.store.VehicleRepository;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/vehicles")
public class VehicleController {

    private final VehicleRepository vehicleRepository;

    public VehicleController(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    @GetMapping
    public List<Vehicle> getVehicles() {
        return vehicleRepository.findAll();
    }

    @GetMapping("/bounds")
    public List<Vehicle> getVehiclesInBounds(
            @RequestParam double minLat,
            @RequestParam double maxLat,
            @RequestParam double minLon,
            @RequestParam double maxLon) {
        return vehicleRepository.findByLatitudeBetweenAndLongitudeBetween(
                minLat, maxLat, minLon, maxLon);
    }

}
