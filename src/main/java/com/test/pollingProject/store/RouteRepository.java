package com.test.pollingProject.store;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.test.pollingProject.model.Route;
import com.test.pollingProject.model.enums.routes.RouteType;

public interface RouteRepository extends JpaRepository<Route, String>, JpaSpecificationExecutor<Route> {

    List<Route> findByRouteId(String route_id);

    List<Route> findByRouteShortName(String routeShortName);

    List<Route> findByAgencyId(String agencyId);

    List<Route> findByRouteType(RouteType routeType);

}
