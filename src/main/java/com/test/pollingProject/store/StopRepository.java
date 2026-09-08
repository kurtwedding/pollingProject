package com.test.pollingProject.store;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.test.pollingProject.model.Stop;

public interface StopRepository extends JpaRepository<Stop, String> {

    List<Stop> findByLatitudeBetweenAndLongitudeBetween(
            double minLat, double maxLat,
            double minLon, double maxLon);
}
