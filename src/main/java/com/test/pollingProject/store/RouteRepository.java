package com.test.pollingProject.store;

import org.springframework.data.jpa.repository.JpaRepository;

import com.test.pollingProject.model.Route;

public interface RouteRepository extends JpaRepository<Route, String> {

}
