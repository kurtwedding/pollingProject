package com.test.pollingProject.store;

import org.springframework.data.jpa.repository.JpaRepository;

import com.test.pollingProject.model.Vehicle;

public interface VehicleRepository extends JpaRepository<Vehicle, String> {

}
