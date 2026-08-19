package com.test.pollingProject.store;

import org.springframework.data.jpa.repository.JpaRepository;

import com.test.pollingProject.model.Trip;

public interface TripRepository extends JpaRepository<Trip, String> {

}
