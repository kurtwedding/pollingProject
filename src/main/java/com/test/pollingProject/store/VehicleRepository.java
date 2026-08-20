package com.test.pollingProject.store;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.test.pollingProject.model.Vehicle;

public interface VehicleRepository extends JpaRepository<Vehicle, String> {
    // Query to find vehicles in the bounds provided
    List<Vehicle> findByLatitudeBetweenAndLongitudeBetween(
            double minLat, double maxLat,
            double minLon, double maxLon);
}
