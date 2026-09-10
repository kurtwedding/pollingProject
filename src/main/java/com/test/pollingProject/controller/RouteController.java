package com.test.pollingProject.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.test.pollingProject.model.Route;
import com.test.pollingProject.model.enums.routes.RouteType;
import com.test.pollingProject.store.RouteRepository;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/routes")
public class RouteController {

    private final RouteRepository routeRepository;

    public RouteController(RouteRepository routeRepository) {
        this.routeRepository = routeRepository;
    }

    @GetMapping()
    public List<Route> getRoutes(
            @RequestParam(required = false) String id,
            @RequestParam(required = false) String shortName,
            @RequestParam(required = false) String agencyId,
            @RequestParam(required = false) String routeType) {

        // Implementing a specification to dynamically filter based on what parameters
        // were provided
        Specification<Route> spec = Specification.unrestricted();

        if (id != null) { // If id was provided, add it to the specification
            spec = spec.and((route, query, cb) -> cb.equal(route.get("agencyId"), agencyId));
        }
        if (shortName != null) { // If shortName was provided, add it to the specification
            spec = spec.and((route, query, cb) -> cb.equal(route.get("routeShortName"), shortName));
        }
        if (agencyId != null) { // If agencyId was provided, add it to the specification
            spec = spec.and((route, query, cb) -> cb.equal(route.get("agencyId"), agencyId));
        }
        if (routeType != null) { // If routeType was provided, add it to the specification
            spec = spec.and((route, query, cb) -> cb.equal(route.get("routeType"), RouteType.fromLabel(routeType)));
        }

        // Return all of the entries that match the specification
        return routeRepository.findAll(spec);
    }

}
