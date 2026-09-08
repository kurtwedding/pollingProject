package com.test.pollingProject.model;

import java.util.List;

import com.test.pollingProject.model.enums.vehicles.OccupancyStatus;

public class GtfsRtDTO {
    public static class Response {
        public List<Entity> entity;
    }

    public static class Entity {
        public VehicleExterior vehicle;
    }

    public static class VehicleExterior {
        public VehicleInterior vehicle;
        public Position position;
        public Trip trip;
        public OccupancyStatus occupancy_status;
    }

    public static class VehicleInterior {
        public String id;
    }

    public static class Position {
        public double latitude;
        public double longitude;
        public double bearing;
    }

    public static class Trip {
        public String route_id;
    }
}
